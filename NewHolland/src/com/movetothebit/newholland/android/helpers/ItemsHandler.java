package com.movetothebit.newholland.android.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Path.Direction;

import com.movetothebit.newholland.android.location.GPSManager;
import com.movetothebit.newholland.android.model.Item;
import com.movetothebit.newholland.android.utils.lConstants;





public 	class ItemsHandler extends DefaultHandler implements lConstants{		

		private Stack mCurrentElement = new Stack();
		private StringBuilder mString;
		public List<Item> result;
		private Item item;
		private String latitude;
		private String longitude;
		private Direction destino;
		private boolean currentElement;
		
		public ItemsHandler() {			
				result = new ArrayList<Item>();
						
				mString = new StringBuilder();
		}

		public void startElement(String uri, String name, String localName,
				Attributes attributes) throws SAXException {
	
 			mCurrentElement.push(localName);
			
			if (localName.equalsIgnoreCase(ITEM)) {		
					item = new Item();					
			}
			
			
		}
		
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			
			String append  = new String(ch, start,length);
			
			if(append.trim()!= null &&append.trim().length()>0){
				mString.append(append);
			}
			
		}

		public void endElement(String uri, String name, String localName)
				throws SAXException {	
			
	
				
			if (mString.length() > 0) {
					System.out.println(localName+":   " +mString.toString());
					if(localName.equalsIgnoreCase(NOMBRE)) {
						item.setName(mString.toString());
					}else if(localName.equalsIgnoreCase(CP)) {
						item.setPostalCode(Integer.valueOf(mString.toString()));
					}else if(localName.equalsIgnoreCase(OBSERVACIONES)) {
						item.setObs(mString.toString());
					}else if(localName.equalsIgnoreCase(DESCRIPCION)) {
						item.setDesc(mString.toString());
					}else if(localName.equalsIgnoreCase(ID)) {
						item.setId(mString.toString());
					}else if(localName.equalsIgnoreCase(CAT_ID)) {
						item.setCatId(mString.toString());
					}else if(localName.equalsIgnoreCase(COUNTRY)) {
						item.setCountry(mString.toString());
					}else if(localName.equalsIgnoreCase(LATITUD)) {
						latitude = mString.toString();
					}else if(localName.equalsIgnoreCase(LONGITUD)) {
						longitude =  mString.toString();
					}else if(localName.equalsIgnoreCase(ADDRESS)) {
						item.setAddress(mString.toString());
					}else if(localName.equalsIgnoreCase(TELEFONOS)) {
						item.setPhone(mString.toString());
					}else if(localName.equalsIgnoreCase(EMAIL)) {						
						item.setEmail(mString.toString());
					}else if(localName.equalsIgnoreCase(TYPE)) {						
						item.setType(Integer.valueOf(mString.toString()));
					}else if(localName.equalsIgnoreCase(IMAGE_URL)) {						
						item.setImageUrl(mString.toString());
					}else if(localName.equalsIgnoreCase(PRICE)) {						
						item.setPrice(mString.toString());
					}else if(localName.equalsIgnoreCase(RATE)) {						
						item.setRate(mString.toString());
					}
					
			}

			mCurrentElement.pop();
	
			if (localName.equalsIgnoreCase(ITEM)) {
				
				item.setLoc(GPSManager.coordinates2Location(Double.parseDouble(latitude), Double.parseDouble(longitude)));
				result.add(item);
			}
			mString.setLength(0);
		}
		
		@Override
		public void fatalError(SAXParseException e) throws SAXException {
			// TODO Auto-generated method stub		

				    // typical, but terrible, error handling

				    // Bring things to a crashing halt
				    System.out.println("**Parsing Fatal Error**" + "\n" +
				                       "  Line:    " + 
				                          e.getLineNumber() + "\n" +
				                       "  URI:     " + 
				                          e.getSystemId() + "\n" +
				                       "  Message: " + 
				                        e.getMessage());        
				    throw new SAXException("Fatal Error encountered");
				
		}


}
