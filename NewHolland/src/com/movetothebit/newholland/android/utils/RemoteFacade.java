package com.movetothebit.newholland.android.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class RemoteFacade {

	
	private static final String TAG_ = "RemoteFacade";	
	private static final int TIMEOUT_MILLISEC = 10000;	
	
	public static String postStringToUrl(String url, String jsonString) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    String _response = null;
	    
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("data", jsonString));
//	        nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
	        Log.i("json Object", jsonString);
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);

	        HttpEntity entity = response.getEntity();
		    InputStream is = entity.getContent();
		    _response = convertStreamToString(is);
		   
		    
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
		return _response;
	} 

	public static String sendJsonToUrl(String url, String jsonString) throws JSONException, ClientProtocolException, IOException {	   

	    HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
	    HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
	    HttpClient client = new DefaultHttpClient(httpParams);
	    
	    HttpPost request = new HttpPost(url); // add your url here...
	    request.setHeader("Accept", "text/html");
	    request.setHeader( "Content-Type", "text/html" );     

	    JSONObject json = new JSONObject(jsonString);  

	    Log.i("json Object", json.toString());

	    StringEntity se = new StringEntity(json.toString());

	    se.setContentEncoding("UTF-8");
	    
	    se.setContentType("text/html");

	    request.setEntity(se);      

	    HttpResponse response = client.execute(request); 

	    HttpEntity entity = response.getEntity();
	    InputStream is = entity.getContent();
	    String _response = convertStreamToString(is);
	    return _response;

	}
	
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8192);
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append((line + "\n"));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	/**
	 * Envia una petición a una dirección de internet y devuelve la salida en un
	 * String
	 * 
	 * @param TAG
	 *            es el TAG de la Actividad llamante. Se usa a efectos de log,
	 *            para identificar quien es el llamante de la operacion.
	 * @param remoteUrl
	 *            La URL a invocar
	 * @return una cadena de texto
	 * @throws SystemException
	 *             excepción de sistema
	 */
	
	public static String stringFromServer(String remoteUrl)
			throws SystemException {
		String res = "";
		HttpURLConnection connection = null;
		BufferedReader rd = null;
		StringBuilder sb = null;
		String line = null;
		URL serverAddress = null;
		try {

			// Log.d(TAG_, TAG_ +
			// ": sendToServer init ---------------------------------- " );
			// Log.d(TAG_, TAG_ + ": sendToServer remoteUrl: " + remoteUrl );

			serverAddress = new URL(remoteUrl);
			connection = null;

			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);

			connection.connect();

			rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "ISO-8859-1"));

			sb = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				sb.append(line + '\n');
			}

			res = sb.toString();	

		} catch (MalformedURLException e) {
			Log.e(TAG_, TAG_ + ": sendToServer " + e);
			throw new SystemException(e, "sendToServer " + e);

		} catch (ProtocolException e) {
			Log.e(TAG_, TAG_ + ": sendToServer " + e);
			throw new SystemException(e, "sendToServer " + e);

		} catch (IOException e) {
			Log.e(TAG_, TAG_ + ": sendToServer " + e);
			throw new SystemException(e, "sendToServer " + e);

		} finally {
			try {
				connection.disconnect();
			} catch (Exception ee) {
			}
			rd = null;
			sb = null;
			connection = null;
		}
		return res;
	}
	/**
	 * Envia una petición a una dirección de internet y devuelve la salida en un
	 * InputStream
	 * 
	 * @param TAG
	 *            es el TAG de la Actividad llamante. Se usa a efectos de log,
	 *            para identificar quien es el llamante de la operacion.
	 * @param remoteUrl
	 *            La URL a invocar
	 * @return una cadena de texto
	 * @throws SystemException
	 *             excepción de sistema
	 */
	 private InputStream retrieveStream(String url) {
	        
	        DefaultHttpClient client = new DefaultHttpClient(); 
	        
	        HttpGet getRequest = new HttpGet(url);
	          
	        try {
	           
	           HttpResponse getResponse = client.execute(getRequest);
	           final int statusCode = getResponse.getStatusLine().getStatusCode();
	           
	           if (statusCode != HttpStatus.SC_OK) { 
	              Log.w(getClass().getSimpleName(), 
	                  "Error " + statusCode + " for URL " + url); 
	              return null;
	           }

	           HttpEntity getResponseEntity = getResponse.getEntity();
	           return getResponseEntity.getContent();
	           
	        } 
	        catch (IOException e) {
	           getRequest.abort();
	           Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
	        }
	        
	        return null;
	        
	}
	 
	/**Metodo que descargara un archivo a el directorio de cache de la aplicacion
	 * @param ctx Contexto de la actividad
	 * @param url url del archivo
	 * @param name nombre como se va a guardar
	 * @return true si ha salido bien, falso si ha habido algun error
	 */
	public static boolean downloadFileToCache(Context ctx ,String url, String name){
		
		URLConnection cn;
    	InputStream stream;
    	File cacheDir;
    	FileOutputStream out; 				
				
				try {
					
					cn = new URL(url).openConnection();
					cn.connect();   
					stream = cn.getInputStream();
					cacheDir = ctx.getCacheDir();
					
					File f=new File(cacheDir, name);
					   // File downloadingMediaFile = new File(cacheDir, "talleres.xml");

					out = new FileOutputStream(f);   
					
					 //this is the total size of the file
			        int totalSize = cn.getContentLength();
			        //variable to store total downloaded bytes
			        int downloadedSize = 0;

			        //create a buffer...
			        byte[] buffer = new byte[1024];
			        int bufferLength = 0; //used to store a temporary size of the buffer

			        //now, read through the input buffer and write the contents to the file
			        while ( (bufferLength = stream.read(buffer)) > 0 ) {
			                //add the data in the buffer to the file in the file output stream (the file on the sd card
			                out.write(buffer, 0, bufferLength);
			                //add up the size so we know how much is downloaded
			                downloadedSize += bufferLength;			           

			        }
			        //close the output stream when done
			        out.close();

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}   		
		
		return true;
	}
	/**
	 * Envia una petición a una dirección de internet y devuelve la salida en un
	 * objecto Document
	 * 
	 * @param TAG
	 *            es el TAG de la Actividad llamante. Se usa a efectos de log,
	 *            para identificar quien es el llamante de la operación.
	 * @param remoteUrl
	 *            La URL a invocar
	 * @return objeto document
	 * @throws SystemException
	 *             excepción de sistema
	 */
	private static Document documentFromServer(String remoteUrl)
			throws SystemException {

		Document document = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			document = builder.parse(new URL(remoteUrl).openStream());
		} catch (ParserConfigurationException e) {
			Log.e(TAG_, TAG_ + ": documentFromServer " + e);
			throw new SystemException(e, "documentFromServer " + e);
		} catch (SAXException e) {
			Log.e(TAG_, TAG_ + ": documentFromServer " + e);
			throw new SystemException(e, "documentFromServer " + e);
		} catch (IOException e) {
			Log.e(TAG_, TAG_ + ": documentFromServer " + e);
			throw new SystemException(e, "documentFromServer " + e);
		}

		return document;

	}
 public void storeFileFromUrl(String urlString, String pathFile){
	 try {
	        //set the download URL, a url that points to a file on the internet
	        //this is the file to be downloaded
	        URL url = new URL(urlString);

	        //create the new connection
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

	        //set up some things on the connection
	        urlConnection.setRequestMethod("GET");
	        urlConnection.setDoOutput(true);

	        //and connect!
	        urlConnection.connect();

	        //set the path where we want to save the file
	        //in this case, going to save it on the root directory of the
	        //sd card.
	        File SDCardRoot = Environment.getExternalStorageDirectory();
	        //create a new file, specifying the path, and the filename
	        //which we want to save the file as.
	        File file = new File(SDCardRoot,pathFile);

	        //this will be used to write the downloaded data into the file we created
	        FileOutputStream fileOutput = new FileOutputStream(file);

	        //this will be used in reading the data from the internet
	        InputStream inputStream = urlConnection.getInputStream();

	        //this is the total size of the file
	        int totalSize = urlConnection.getContentLength();
	        //variable to store total downloaded bytes
	        int downloadedSize = 0;

	        //create a buffer...
	        byte[] buffer = new byte[1024];
	        int bufferLength = 0; //used to store a temporary size of the buffer

	        //now, read through the input buffer and write the contents to the file
	        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	                //add the data in the buffer to the file in the file output stream (the file on the sd card
	                fileOutput.write(buffer, 0, bufferLength);
	                //add up the size so we know how much is downloaded
	                downloadedSize += bufferLength;
	                //this is where you would do something to report the prgress, like this maybe
	               // updateProgress(downloadedSize, totalSize);

	        }
	        //close the output stream when done
	        fileOutput.close();

	//catch some possible errors...
	} catch (MalformedURLException e) {
	        e.printStackTrace();
	} catch (IOException e) {
	        e.printStackTrace();
	}
 }
	/**
	 * Extrae un String con el nombre tagName de un Element
	 * @param element elemento del que voy a sacar el String
	 * @param tagName tag del atributo que voy a sacar el string
	 * @return res String que contiene 
	 */
	private static String getElementText(Node element, String tagName) {
		String res = "";
		if (element != null && tagName != null) {
			NodeList nl = ((Element) element).getElementsByTagName(tagName);
			if (nl != null && nl.item(0) != null) {
				Node n = nl.item(0);
				NodeList nlc = n.getChildNodes();
				if (nlc != null && nlc.item(0) != null)
					res = nlc.item(0).getNodeValue();
			}
		}
		return res;
	}

	
	  
	


}
	
