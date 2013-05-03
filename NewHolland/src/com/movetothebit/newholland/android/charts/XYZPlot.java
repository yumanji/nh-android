package com.movetothebit.newholland.android.charts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.afree.chart.LegendItem;
import org.afree.chart.LegendItemCollection;
import org.afree.chart.annotations.XYAnnotation;
import org.afree.chart.annotations.XYAnnotationBoundsInfo;
import org.afree.chart.axis.Axis;
import org.afree.chart.axis.AxisCollection;
import org.afree.chart.axis.AxisLocation;
import org.afree.chart.axis.AxisSpace;
import org.afree.chart.axis.AxisState;
import org.afree.chart.axis.TickType;
import org.afree.chart.axis.ValueAxis;
import org.afree.chart.axis.ValueTick;
import org.afree.chart.event.ChartChangeEventType;
import org.afree.chart.event.PlotChangeEvent;
import org.afree.chart.event.RendererChangeEvent;
import org.afree.chart.event.RendererChangeListener;
import org.afree.chart.plot.CrosshairState;
import org.afree.chart.plot.DatasetRenderingOrder;
import org.afree.chart.plot.Marker;
import org.afree.chart.plot.Pannable;
import org.afree.chart.plot.Plot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.PlotRenderingInfo;
import org.afree.chart.plot.PlotState;
import org.afree.chart.plot.SeriesRenderingOrder;
import org.afree.chart.plot.ValueAxisPlot;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.plot.Zoomable;
import org.afree.chart.renderer.RendererUtilities;
import org.afree.chart.renderer.xy.AbstractXYItemRenderer;
import org.afree.chart.renderer.xy.XYItemRenderer;
import org.afree.chart.renderer.xy.XYItemRendererState;
import org.afree.data.Range;
import org.afree.data.general.DatasetChangeEvent;
import org.afree.data.general.DatasetUtilities;
import org.afree.data.xy.XYDataset;
import org.afree.graphics.PaintType;
import org.afree.graphics.PaintUtility;
import org.afree.graphics.SolidColor;
import org.afree.graphics.geom.LineShape;
import org.afree.graphics.geom.RectShape;
import org.afree.ui.Layer;
import org.afree.ui.RectangleEdge;
import org.afree.ui.RectangleInsets;
import org.afree.util.ObjectList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;

/**
 * A general class for plotting data in the form of (x, y) pairs. This plot can
 * use data from any class that implements the {@link XYDataset} interface.
 * <P>
 * <code>XYPlot</code> makes use of an {@link XYItemRenderer} to draw each point
 * on the plot. By using different renderers, various chart types can be
 * produced.
 * <p>
 * The {@link org.afree.chart.ChartFactory} class contains static methods for
 * creating pre-configured charts.
 */
public class XYZPlot extends XYPlot implements ValueAxisPlot, Pannable, Zoomable,
        RendererChangeListener, Cloneable {

    /** For serialization. */
    private static final long serialVersionUID = 7044148245716569264L;

    /** The default grid line stroke. */
    public static final Float DEFAULT_GRIDLINE_STROKE = 1f;

    /** The default grid line paint. */
    public static final PaintType DEFAULT_GRIDLINE_PAINT = new SolidColor(Color.LTGRAY);
    
    /** The default grid line effect. */
    public static final PathEffect DEFAULT_GRIDLINE_EFFECT = new DashPathEffect(new float[]{3.0f, 2.0f}, 0);
    
    /** The default crosshair visibility. */
    public static final boolean DEFAULT_CROSSHAIR_VISIBLE = false;

    /** The default crosshair stroke. */
    public static final Float DEFAULT_CROSSHAIR_STROKE = DEFAULT_GRIDLINE_STROKE;

    /** The default crosshair paint. */
    public static final PaintType DEFAULT_CROSSHAIR_PAINT = new SolidColor(Color.BLUE);
    
    /** The default crosshair effect. */
    public static final PathEffect DEFAULT_CROSSHAIR_EFFECT = new DashPathEffect(new float[]{3.0f, 2.0f}, 0);

    /** The plot orientation. */
    private PlotOrientation orientation;

    /** The offset between the data area and the axes. */
    private RectangleInsets axisOffset;

    /** The domain axis / axes (used for the x-values). */
    private ObjectList domainAxes;

    /** The domain axis locations. */
    private ObjectList domainAxisLocations;

    /** The range axis (used for the y-values). */
    private ObjectList rangeAxes;

    /** The range axis location. */
    private ObjectList rangeAxisLocations;

    /** Storage for the datasets. */
    private ObjectList datasets;

    /** Storage for the renderers. */
    private ObjectList renderers;

    /**
     * Storage for the mapping between datasets/renderers and domain axes. The
     * keys in the map are Integer objects, corresponding to the dataset index.
     * The values in the map are List objects containing Integer objects
     * (corresponding to the axis indices). If the map contains no entry for a
     * dataset, it is assumed to map to the primary domain axis (index = 0).
     */
    private Map datasetToDomainAxesMap;

    /**
     * Storage for the mapping between datasets/renderers and range axes. The
     * keys in the map are Integer objects, corresponding to the dataset index.
     * The values in the map are List objects containing Integer objects
     * (corresponding to the axis indices). If the map contains no entry for a
     * dataset, it is assumed to map to the primary domain axis (index = 0).
     */
    private Map datasetToRangeAxesMap;

    /** The origin point for the quadrants (if drawn). */
    private transient PointF quadrantOrigin = new PointF(0.0f, 0.0f);

    /** The paint used for each quadrant. */
    private transient PaintType[] quadrantPaint = new PaintType[] { null, null, null,
            null };

    /** A flag that controls whether the domain grid-lines are visible. */
    private boolean domainGridlinesVisible;

    /** The stroke used to draw the domain grid-lines. */
    private transient Float domainGridlineStroke;

    /** The paint used to draw the domain grid-lines. */
    private transient PaintType domainGridlinePaintType;
    
    /** The effect used to draw the domain grid-lines. */
    private transient PathEffect domainGridlineEffect;

    /** A flag that controls whether the range grid-lines are visible. */
    private boolean rangeGridlinesVisible;

    /** The stroke used to draw the range grid-lines. */
    private transient Float rangeGridlineStroke;

    /** The paint used to draw the range grid-lines. */
    private transient PaintType rangeGridlinePaintType;
    
    /** The effect used to draw the range grid-lines. */
    private transient PathEffect rangeGridlineEffect;

    /**
     * A flag that controls whether the domain minor grid-lines are visible.
     * 
     * @since JFreeChart 1.0.12
     */
    private boolean domainMinorGridlinesVisible;

    /**
     * The stroke used to draw the domain minor grid-lines.
     * 
     * @since JFreeChart 1.0.12
     */
    private transient Float domainMinorGridlineStroke;

    /**
     * The paint used to draw the domain minor grid-lines.
     * 
     * @since JFreeChart 1.0.12
     */
    private transient PaintType domainMinorGridlinePaint;

    /** The effect used to draw the domain minor grid-lines. */
    private transient PathEffect domainMinorGridlineEffect;

    /**
     * A flag that controls whether the range minor grid-lines are visible.
     * 
     * @since JFreeChart 1.0.12
     */
    private boolean rangeMinorGridlinesVisible;

    /**
     * The stroke used to draw the range minor grid-lines.
     * 
     * @since JFreeChart 1.0.12
     */
    private transient Float rangeMinorGridlineStroke;

    /**
     * The paint used to draw the range minor grid-lines.
     * 
     * @since JFreeChart 1.0.12
     */
    private transient PaintType rangeMinorGridlinePaint;

    /** The effect used to draw the range minor grid-lines. */
    private transient PathEffect rangeMinorGridlineEffect;

    /**
     * A flag that controls whether or not the zero baseline against the domain
     * axis is visible.
     * 
     * @since JFreeChart 1.0.5
     */
    private boolean domainZeroBaselineVisible;

    /**
     * The stroke used for the zero baseline against the domain axis.
     * 
     * @since JFreeChart 1.0.5
     */
    private transient Float domainZeroBaselineStroke;

    /**
     * The paint used for the zero baseline against the domain axis.
     * 
     * @since JFreeChart 1.0.5
     */
    private transient PaintType domainZeroBaselinePaint;

    /** The effect used for the zero baseline against the domain axis. */
    private transient PathEffect domainZeroBaselineEffect;

    /**
     * A flag that controls whether or not the zero baseline against the range
     * axis is visible.
     */
    private boolean rangeZeroBaselineVisible;

    /** The stroke used for the zero baseline against the range axis. */
    private transient Float rangeZeroBaselineStroke;

    /** The paint used for the zero baseline against the range axis. */
    private transient PaintType rangeZeroBaselinePaint;
    
    /** The effect used for the zero baseline against the range axis. */
    private transient PathEffect rangeZeroBaselineEffect;

    /** A flag that controls whether or not a domain crosshair is drawn.. */
    private boolean domainCrosshairVisible;

    /** The domain crosshair value. */
    private double domainCrosshairValue;

    /** The pen/brush used to draw the crosshair (if any). */
    private transient Float domainCrosshairStroke;

    /** The color used to draw the crosshair (if any). */
    private transient PaintType domainCrosshairPaintType;
    
    /** The effect used to draw the crosshair (if any). */
    private transient PathEffect domainCrosshairEffect;

    /**
     * A flag that controls whether or not the crosshair locks onto actual data
     * points.
     */
    private boolean domainCrosshairLockedOnData = true;

    /** A flag that controls whether or not a range crosshair is drawn.. */
    private boolean rangeCrosshairVisible;

    /** The range crosshair value. */
    private double rangeCrosshairValue;

    /** The pen/brush used to draw the crosshair (if any). */
    private transient Float rangeCrosshairStroke;

    /** The color used to draw the crosshair (if any). */
    private transient PaintType rangeCrosshairPaintType;
    
    /** The effect used to draw the crosshair (if any). */
    private transient PathEffect rangeCrosshairEffect;

    /**
     * A flag that controls whether or not the crosshair locks onto actual data
     * points.
     */
    private boolean rangeCrosshairLockedOnData = true;

    /** A map of lists of foreground markers (optional) for the domain axes. */
    private Map foregroundDomainMarkers;

    /** A map of lists of background markers (optional) for the domain axes. */
    private Map backgroundDomainMarkers;

    /** A map of lists of foreground markers (optional) for the range axes. */
    private Map foregroundRangeMarkers;

    /** A map of lists of background markers (optional) for the range axes. */
    private Map backgroundRangeMarkers;

    /**
     * A (possibly empty) list of annotations for the plot. The list should be
     * initialised in the constructor and never allowed to be <code>null</code>.
     */
    private List annotations;

    /** The paint used for the domain tick bands (if any). */
    private transient PaintType domainTickBandPaint;

    /** The paint used for the range tick bands (if any). */
    private transient PaintType rangeTickBandPaint;

    /** The fixed domain axis space. */
    private AxisSpace fixedDomainAxisSpace;

    /** The fixed range axis space. */
    private AxisSpace fixedRangeAxisSpace;

    /**
     * The order of the dataset rendering (REVERSE draws the primary dataset
     * last so that it appears to be on top).
     */
    private DatasetRenderingOrder datasetRenderingOrder = DatasetRenderingOrder.REVERSE;

    /**
     * The order of the series rendering (REVERSE draws the primary series last
     * so that it appears to be on top).
     */
    private SeriesRenderingOrder seriesRenderingOrder = SeriesRenderingOrder.REVERSE;

    /**
     * The weight for this plot (only relevant if this is a subplot in a
     * combined plot).
     */
    private int weight;

    /**
     * An optional collection of legend items that can be returned by the
     * getLegendItems() method.
     */
    private LegendItemCollection fixedLegendItems;

    /**
     * A flag that controls whether or not panning is enabled for the domain
     * axis/axes.
     * 
     * @since JFreeChart 1.0.13
     */
    private boolean domainPannable;

    /**
     * A flag that controls whether or not panning is enabled for the range
     * axis/axes.
     * 
     * @since JFreeChart 1.0.13
     */
    private boolean rangePannable;

    /**
     * Creates a new <code>XYPlot</code> instance with no dataset, no axes and
     * no renderer. You should specify these items before using the plot.
     */
    public XYZPlot() {
        this(null, null, null, null);
    }

    /**
     * Creates a new plot with the specified dataset, axes and renderer. Any of
     * the arguments can be <code>null</code>, but in that case you should take
     * care to specify the value before using the plot (otherwise a
     * <code>NullPointerException</code> may be thrown).
     * 
     * @param dataset
     *            the dataset (<code>null</code> permitted).
     * @param domainAxis
     *            the domain axis (<code>null</code> permitted).
     * @param rangeAxis
     *            the range axis (<code>null</code> permitted).
     * @param renderer
     *            the renderer (<code>null</code> permitted).
     */
    public XYZPlot(XYDataset dataset, ValueAxis domainAxis, ValueAxis rangeAxis,
            XYItemRenderer renderer) {

        super();

        this.orientation = PlotOrientation.VERTICAL;
        this.weight = 1; // only relevant when this is a subplot
        this.axisOffset = RectangleInsets.ZERO_INSETS;

        // allocate storage for datasets, axes and renderers (all optional)
        this.domainAxes = new ObjectList();
        this.domainAxisLocations = new ObjectList();
        this.foregroundDomainMarkers = new HashMap();
        this.backgroundDomainMarkers = new HashMap();

        this.rangeAxes = new ObjectList();
        this.rangeAxisLocations = new ObjectList();
        this.foregroundRangeMarkers = new HashMap();
        this.backgroundRangeMarkers = new HashMap();

        this.datasets = new ObjectList();
        this.renderers = new ObjectList();

        this.datasetToDomainAxesMap = new TreeMap();
        this.datasetToRangeAxesMap = new TreeMap();

        this.annotations = new java.util.ArrayList();

        this.datasets.set(0, dataset);
        if (dataset != null) {
             dataset.addChangeListener(this);
        }

        this.renderers.set(0, renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener(this);
        }

        this.domainAxes.set(0, domainAxis);
        this.mapDatasetToDomainAxis(0, 0);
        if (domainAxis != null) {
            domainAxis.setPlot(this);
             domainAxis.addChangeListener(this);
        }
        this.domainAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);

        this.rangeAxes.set(0, rangeAxis);
        this.mapDatasetToRangeAxis(0, 0);
        if (rangeAxis != null) {
            rangeAxis.setPlot(this);
             rangeAxis.addChangeListener(this);
        }
        this.rangeAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);

        configureDomainAxes();
        configureRangeAxes();

        this.domainGridlinesVisible = true;
        this.domainGridlineStroke = DEFAULT_GRIDLINE_STROKE;
        this.domainGridlinePaintType = DEFAULT_GRIDLINE_PAINT;
        this.domainGridlineEffect = DEFAULT_GRIDLINE_EFFECT;

        this.domainMinorGridlinesVisible = false;
        this.domainMinorGridlineStroke = DEFAULT_GRIDLINE_STROKE;
        this.domainMinorGridlinePaint = DEFAULT_GRIDLINE_PAINT;
        this.domainMinorGridlineEffect = DEFAULT_GRIDLINE_EFFECT;

        this.domainZeroBaselineVisible = false;
        this.domainZeroBaselinePaint = new SolidColor(Color.BLACK);
        this.domainZeroBaselineStroke = 1f;

        this.rangeGridlinesVisible = true;
        this.rangeGridlineStroke = DEFAULT_GRIDLINE_STROKE;
        this.rangeGridlinePaintType = DEFAULT_GRIDLINE_PAINT;
        this.rangeGridlineEffect = DEFAULT_GRIDLINE_EFFECT;

        this.rangeMinorGridlinesVisible = false;
        this.rangeMinorGridlineStroke = DEFAULT_GRIDLINE_STROKE;
        this.rangeMinorGridlinePaint = DEFAULT_GRIDLINE_PAINT;
        this.rangeMinorGridlineEffect = DEFAULT_GRIDLINE_EFFECT;

        this.rangeZeroBaselineVisible = false;
        this.rangeZeroBaselinePaint = new SolidColor(Color.BLACK);
        this.rangeZeroBaselineStroke = 1f;

        this.domainCrosshairVisible = false;
        this.domainCrosshairValue = 0.0;
        this.domainCrosshairStroke = DEFAULT_CROSSHAIR_STROKE;
        this.domainCrosshairPaintType = DEFAULT_CROSSHAIR_PAINT;
        this.domainCrosshairEffect = DEFAULT_CROSSHAIR_EFFECT;

        this.rangeCrosshairVisible = false;
        this.rangeCrosshairValue = 0.0;
        this.rangeCrosshairStroke = DEFAULT_CROSSHAIR_STROKE;
        this.rangeCrosshairPaintType = DEFAULT_CROSSHAIR_PAINT;
        this.rangeCrosshairEffect = DEFAULT_CROSSHAIR_EFFECT;

    }

    /**
     * Returns the plot type as a string.
     * 
     * @return A short string describing the type of plot.
     */
    public String getPlotType() {
        return "XY_Plot";
    }

    /**
     * Returns the orientation of the plot.
     * 
     * @return The orientation (never <code>null</code>).
     * 
     * @see #setOrientation(PlotOrientation)
     */
    public PlotOrientation getOrientation() {
        return this.orientation;
    }

    /**
     * Sets the orientation for the plot and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param orientation
     *            the orientation (<code>null</code> not allowed).
     * 
     * @see #getOrientation()
     */
    public void setOrientation(PlotOrientation orientation) {
        if (orientation == null) {
            throw new IllegalArgumentException("Null 'orientation' argument.");
        }
        if (orientation != this.orientation) {
            this.orientation = orientation;
            fireChangeEvent();
        }
    }

    /**
     * Returns the axis offset.
     * 
     * @return The axis offset (never <code>null</code>).
     * 
     * @see #setAxisOffset(RectangleInsets)
     */
    public RectangleInsets getAxisOffset() {
        return this.axisOffset;
    }

    /**
     * Sets the axis offsets (gap between the data area and the axes) and sends
     * a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param offset
     *            the offset (<code>null</code> not permitted).
     * 
     * @see #getAxisOffset()
     */
    public void setAxisOffset(RectangleInsets offset) {
        if (offset == null) {
            throw new IllegalArgumentException("Null 'offset' argument.");
        }
        this.axisOffset = offset;
        fireChangeEvent();
    }

    /**
     * Returns the domain axis with index 0. If the domain axis for this plot is
     * <code>null</code>, then the method will return the parent plot's domain
     * axis (if there is a parent plot).
     * 
     * @return The domain axis (possibly <code>null</code>).
     * 
     * @see #getDomainAxis(int)
     * @see #setDomainAxis(ValueAxis)
     */
    public ValueAxis getDomainAxis() {
        return getDomainAxis(0);
    }

    /**
     * Returns the domain axis with the specified index, or <code>null</code>.
     * 
     * @param index
     *            the axis index.
     * 
     * @return The axis (<code>null</code> possible).
     * 
     * @see #setDomainAxis(int, ValueAxis)
     */
    public ValueAxis getDomainAxis(int index) {
        ValueAxis result = null;
        if (index < this.domainAxes.size()) {
            result = (ValueAxis) this.domainAxes.get(index);
        }
        if (result == null) {
            Plot parent = getParent();
            if (parent instanceof XYPlot) {
                XYPlot xy = (XYPlot) parent;
                result = xy.getDomainAxis(index);
            }
        }
        return result;
    }

    /**
     * Sets the domain axis for the plot and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param axis
     *            the new axis (<code>null</code> permitted).
     * 
     * @see #getDomainAxis()
     * @see #setDomainAxis(int, ValueAxis)
     */
    public void setDomainAxis(ValueAxis axis) {
        setDomainAxis(0, axis);
    }

    /**
     * Sets a domain axis and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     * 
     * @param index
     *            the axis index.
     * @param axis
     *            the axis (<code>null</code> permitted).
     * 
     * @see #getDomainAxis(int)
     * @see #setRangeAxis(int, ValueAxis)
     */
    public void setDomainAxis(int index, ValueAxis axis) {
        setDomainAxis(index, axis, true);
    }

    /**
     * Sets a domain axis and, if requested, sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param index
     *            the axis index.
     * @param axis
     *            the axis.
     * @param notify
     *            notify listeners?
     * 
     * @see #getDomainAxis(int)
     */
    public void setDomainAxis(int index, ValueAxis axis, boolean notify) {
        ValueAxis existing = getDomainAxis(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        if (axis != null) {
            axis.setPlot(this);
        }
        this.domainAxes.set(index, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Sets the domain axes for this plot and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param axes
     *            the axes (<code>null</code> not permitted).
     * 
     * @see #setRangeAxes(ValueAxis[])
     */
    public void setDomainAxes(ValueAxis[] axes) {
        for (int i = 0; i < axes.length; i++) {
            setDomainAxis(i, axes[i], false);
        }
        fireChangeEvent();
    }

    /**
     * Returns the location of the primary domain axis.
     * 
     * @return The location (never <code>null</code>).
     * 
     * @see #setDomainAxisLocation(AxisLocation)
     */
    public AxisLocation getDomainAxisLocation() {
        return (AxisLocation) this.domainAxisLocations.get(0);
    }

    /**
     * Sets the location of the primary domain axis and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param location
     *            the location (<code>null</code> not permitted).
     * 
     * @see #getDomainAxisLocation()
     */
    public void setDomainAxisLocation(AxisLocation location) {
        // delegate...
        setDomainAxisLocation(0, location, true);
    }

    /**
     * Sets the location of the domain axis and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param location
     *            the location (<code>null</code> not permitted).
     * @param notify
     *            notify listeners?
     * 
     * @see #getDomainAxisLocation()
     */
    public void setDomainAxisLocation(AxisLocation location, boolean notify) {
        // delegate...
        setDomainAxisLocation(0, location, notify);
    }

    /**
     * Returns the edge for the primary domain axis (taking into account the
     * plot's orientation).
     * 
     * @return The edge.
     * 
     * @see #getDomainAxisLocation()
     * @see #getOrientation()
     */
    public RectangleEdge getDomainAxisEdge() {
        return Plot.resolveDomainAxisLocation(getDomainAxisLocation(),
                this.orientation);
    }

    /**
     * Returns the number of domain axes.
     * 
     * @return The axis count.
     * 
     * @see #getRangeAxisCount()
     */
    public int getDomainAxisCount() {
        return this.domainAxes.size();
    }

    /**
     * Clears the domain axes from the plot and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     * 
     * @see #clearRangeAxes()
     */
    public void clearDomainAxes() {
        for (int i = 0; i < this.domainAxes.size(); i++) {
            ValueAxis axis = (ValueAxis) this.domainAxes.get(i);
            if (axis != null) {
                axis.removeChangeListener(this);
            }
        }
        this.domainAxes.clear();
        fireChangeEvent();
    }

    /**
     * Configures the domain axes.
     */
    public void configureDomainAxes() {
        for (int i = 0; i < this.domainAxes.size(); i++) {
            ValueAxis axis = (ValueAxis) this.domainAxes.get(i);
            if (axis != null) {
                axis.configure();
            }
        }
    }

    /**
     * Returns the location for a domain axis. If this hasn't been set
     * explicitly, the method returns the location that is opposite to the
     * primary domain axis location.
     * 
     * @param index
     *            the axis index.
     * 
     * @return The location (never <code>null</code>).
     * 
     * @see #setDomainAxisLocation(int, AxisLocation)
     */
    public AxisLocation getDomainAxisLocation(int index) {
        AxisLocation result = null;
        if (index < this.domainAxisLocations.size()) {
            result = (AxisLocation) this.domainAxisLocations.get(index);
        }
        if (result == null) {
            result = AxisLocation.getOpposite(getDomainAxisLocation());
        }
        return result;
    }

    /**
     * Sets the location for a domain axis and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     * 
     * @param index
     *            the axis index.
     * @param location
     *            the location (<code>null</code> not permitted for index 0).
     * 
     * @see #getDomainAxisLocation(int)
     */
    public void setDomainAxisLocation(int index, AxisLocation location) {
        // delegate...
        setDomainAxisLocation(index, location, true);
    }

    /**
     * Sets the axis location for a domain axis and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param index
     *            the axis index.
     * @param location
     *            the location (<code>null</code> not permitted for index 0).
     * @param notify
     *            notify listeners?
     * 
     * @since JFreeChart 1.0.5
     * 
     * @see #getDomainAxisLocation(int)
     * @see #setRangeAxisLocation(int, AxisLocation, boolean)
     */
    public void setDomainAxisLocation(int index, AxisLocation location,
            boolean notify) {

        if (index == 0 && location == null) {
            throw new IllegalArgumentException(
                    "Null 'location' for index 0 not permitted.");
        }
        this.domainAxisLocations.set(index, location);
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the edge for a domain axis.
     * 
     * @param index
     *            the axis index.
     * 
     * @return The edge.
     * 
     * @see #getRangeAxisEdge(int)
     */
    public RectangleEdge getDomainAxisEdge(int index) {
        AxisLocation location = getDomainAxisLocation(index);
        RectangleEdge result = Plot.resolveDomainAxisLocation(location,
                this.orientation);
        if (result == null) {
            result = RectangleEdge.opposite(getDomainAxisEdge());
        }
        return result;
    }

    /**
     * Returns the range axis for the plot. If the range axis for this plot is
     * <code>null</code>, then the method will return the parent plot's range
     * axis (if there is a parent plot).
     * 
     * @return The range axis.
     * 
     * @see #getRangeAxis(int)
     * @see #setRangeAxis(ValueAxis)
     */
    public ValueAxis getRangeAxis() {
        return getRangeAxis(0);
    }

    /**
     * Sets the range axis for the plot and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param axis
     *            the axis (<code>null</code> permitted).
     * 
     * @see #getRangeAxis()
     * @see #setRangeAxis(int, ValueAxis)
     */
    public void setRangeAxis(ValueAxis axis) {

        if (axis != null) {
            axis.setPlot(this);
        }

        // plot is likely registered as a listener with the existing axis...
        ValueAxis existing = getRangeAxis();
        if (existing != null) {
            existing.removeChangeListener(this);
        }

        this.rangeAxes.set(0, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        fireChangeEvent();

    }

    /**
     * Returns the location of the primary range axis.
     * 
     * @return The location (never <code>null</code>).
     * 
     * @see #setRangeAxisLocation(AxisLocation)
     */
    public AxisLocation getRangeAxisLocation() {
        return (AxisLocation) this.rangeAxisLocations.get(0);
    }

    /**
     * Sets the location of the primary range axis and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param location
     *            the location (<code>null</code> not permitted).
     * 
     * @see #getRangeAxisLocation()
     */
    public void setRangeAxisLocation(AxisLocation location) {
        // delegate...
        setRangeAxisLocation(0, location, true);
    }

    /**
     * Sets the location of the primary range axis and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param location
     *            the location (<code>null</code> not permitted).
     * @param notify
     *            notify listeners?
     * 
     * @see #getRangeAxisLocation()
     */
    public void setRangeAxisLocation(AxisLocation location, boolean notify) {
        // delegate...
        setRangeAxisLocation(0, location, notify);
    }

    /**
     * Returns the edge for the primary range axis.
     * 
     * @return The range axis edge.
     * 
     * @see #getRangeAxisLocation()
     * @see #getOrientation()
     */
    public RectangleEdge getRangeAxisEdge() {
        return Plot.resolveRangeAxisLocation(getRangeAxisLocation(),
                this.orientation);
    }

    /**
     * Returns a range axis.
     * 
     * @param index
     *            the axis index.
     * 
     * @return The axis (<code>null</code> possible).
     * 
     * @see #setRangeAxis(int, ValueAxis)
     */
    public ValueAxis getRangeAxis(int index) {
        ValueAxis result = null;
        if (index < this.rangeAxes.size()) {
            result = (ValueAxis) this.rangeAxes.get(index);
        }
        if (result == null) {
            Plot parent = getParent();
            if (parent instanceof XYPlot) {
                XYPlot xy = (XYPlot) parent;
                result = xy.getRangeAxis(index);
            }
        }
        return result;
    }

    /**
     * Sets a range axis and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     * 
     * @param index
     *            the axis index.
     * @param axis
     *            the axis (<code>null</code> permitted).
     * 
     * @see #getRangeAxis(int)
     */
    public void setRangeAxis(int index, ValueAxis axis) {
        setRangeAxis(index, axis, true);
    }

    /**
     * Sets a range axis and, if requested, sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param index
     *            the axis index.
     * @param axis
     *            the axis (<code>null</code> permitted).
     * @param notify
     *            notify listeners?
     * 
     * @see #getRangeAxis(int)
     */
    public void setRangeAxis(int index, ValueAxis axis, boolean notify) {
        ValueAxis existing = getRangeAxis(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        if (axis != null) {
            axis.setPlot(this);
        }
        this.rangeAxes.set(index, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Sets the range axes for this plot and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param axes
     *            the axes (<code>null</code> not permitted).
     * 
     * @see #setDomainAxes(ValueAxis[])
     */
    public void setRangeAxes(ValueAxis[] axes) {
        for (int i = 0; i < axes.length; i++) {
            setRangeAxis(i, axes[i], false);
        }
        fireChangeEvent();
    }

    /**
     * Returns the number of range axes.
     * 
     * @return The axis count.
     * 
     * @see #getDomainAxisCount()
     */
    public int getRangeAxisCount() {
        return this.rangeAxes.size();
    }

    /**
     * Clears the range axes from the plot and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     * 
     * @see #clearDomainAxes()
     */
    public void clearRangeAxes() {
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            ValueAxis axis = (ValueAxis) this.rangeAxes.get(i);
            if (axis != null) {
                axis.removeChangeListener(this);
            }
        }
        this.rangeAxes.clear();
        fireChangeEvent();
    }

    /**
     * Configures the range axes.
     * 
     * @see #configureDomainAxes()
     */
    public void configureRangeAxes() {
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            ValueAxis axis = (ValueAxis) this.rangeAxes.get(i);
            if (axis != null) {
                axis.configure();
            }
        }
    }

    /**
     * Returns the location for a range axis. If this hasn't been set
     * explicitly, the method returns the location that is opposite to the
     * primary range axis location.
     * 
     * @param index
     *            the axis index.
     * 
     * @return The location (never <code>null</code>).
     * 
     * @see #setRangeAxisLocation(int, AxisLocation)
     */
    public AxisLocation getRangeAxisLocation(int index) {
        AxisLocation result = null;
        if (index < this.rangeAxisLocations.size()) {
            result = (AxisLocation) this.rangeAxisLocations.get(index);
        }
        if (result == null) {
            result = AxisLocation.getOpposite(getRangeAxisLocation());
        }
        return result;
    }

    /**
     * Sets the location for a range axis and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param index
     *            the axis index.
     * @param location
     *            the location (<code>null</code> permitted).
     * 
     * @see #getRangeAxisLocation(int)
     */
    public void setRangeAxisLocation(int index, AxisLocation location) {
        // delegate...
        setRangeAxisLocation(index, location, true);
    }

    /**
     * Sets the axis location for a domain axis and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param index
     *            the axis index.
     * @param location
     *            the location (<code>null</code> not permitted for index 0).
     * @param notify
     *            notify listeners?
     * 
     * @since JFreeChart 1.0.5
     * 
     * @see #getRangeAxisLocation(int)
     * @see #setDomainAxisLocation(int, AxisLocation, boolean)
     */
    public void setRangeAxisLocation(int index, AxisLocation location,
            boolean notify) {

        if (index == 0 && location == null) {
            throw new IllegalArgumentException(
                    "Null 'location' for index 0 not permitted.");
        }
        this.rangeAxisLocations.set(index, location);
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the edge for a range axis.
     * 
     * @param index
     *            the axis index.
     * 
     * @return The edge.
     * 
     * @see #getRangeAxisLocation(int)
     * @see #getOrientation()
     */
    public RectangleEdge getRangeAxisEdge(int index) {
        AxisLocation location = getRangeAxisLocation(index);
        RectangleEdge result = Plot.resolveRangeAxisLocation(location,
                this.orientation);
        if (result == null) {
            result = RectangleEdge.opposite(getRangeAxisEdge());
        }
        return result;
    }

    /**
     * Returns the primary dataset for the plot.
     * 
     * @return The primary dataset (possibly <code>null</code>).
     * 
     * @see #getDataset(int)
     * @see #setDataset(XYDataset)
     */
    public XYDataset getDataset() {
        return getDataset(0);
    }

    /**
     * Returns a dataset.
     * 
     * @param index
     *            the dataset index.
     * 
     * @return The dataset (possibly <code>null</code>).
     * 
     * @see #setDataset(int, XYDataset)
     */
    public XYDataset getDataset(int index) {
        XYDataset result = null;
        if (this.datasets.size() > index) {
            result = (XYDataset) this.datasets.get(index);
        }
        return result;
    }

    /**
     * Sets the primary dataset for the plot, replacing the existing dataset if
     * there is one.
     * 
     * @param dataset
     *            the dataset (<code>null</code> permitted).
     * 
     * @see #getDataset()
     * @see #setDataset(int, XYDataset)
     */
    public void setDataset(XYDataset dataset) {
        setDataset(0, dataset);
    }

    /**
     * Sets a dataset for the plot.
     * 
     * @param index
     *            the dataset index.
     * @param dataset
     *            the dataset (<code>null</code> permitted).
     * 
     * @see #getDataset(int)
     */
    public void setDataset(int index, XYDataset dataset) {
        XYDataset existing = getDataset(index);
        if (existing != null) {
             existing.removeChangeListener(this);
        }
        this.datasets.set(index, dataset);
        if (dataset != null) {
             dataset.addChangeListener(this);
        }

        // send a dataset change event to self...
         DatasetChangeEvent event = new DatasetChangeEvent(this, dataset);
         datasetChanged(event);
    }

    /**
     * Returns the number of datasets.
     * 
     * @return The number of datasets.
     */
    public int getDatasetCount() {
        return this.datasets.size();
    }

    /**
     * Returns the index of the specified dataset, or <code>-1</code> if the
     * dataset does not belong to the plot.
     * 
     * @param dataset
     *            the dataset (<code>null</code> not permitted).
     * 
     * @return The index.
     */
    public int indexOf(XYDataset dataset) {
        int result = -1;
        for (int i = 0; i < this.datasets.size(); i++) {
            if (dataset == this.datasets.get(i)) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * Maps a dataset to a particular domain axis. All data will be plotted
     * against axis zero by default, no mapping is required for this case.
     * 
     * @param index
     *            the dataset index (zero-based).
     * @param axisIndex
     *            the axis index.
     * 
     * @see #mapDatasetToRangeAxis(int, int)
     */
    public void mapDatasetToDomainAxis(int index, int axisIndex) {
        List axisIndices = new java.util.ArrayList(1);
        axisIndices.add(new Integer(axisIndex));
        mapDatasetToDomainAxes(index, axisIndices);
    }

    /**
     * Maps the specified dataset to the axes in the list. Note that the
     * conversion of data values into Java2D space is always performed using the
     * first axis in the list.
     * 
     * @param index
     *            the dataset index (zero-based).
     * @param axisIndices
     *            the axis indices (<code>null</code> permitted).
     * 
     * @since JFreeChart 1.0.12
     */
    public void mapDatasetToDomainAxes(int index, List axisIndices) {
        if (index < 0) {
            throw new IllegalArgumentException("Requires 'index' >= 0.");
        }
        checkAxisIndices(axisIndices);
        Integer key = new Integer(index);
        this.datasetToDomainAxesMap.put(key, new ArrayList(axisIndices));
        // fake a dataset change event to update axes...
         datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }

    /**
     * Maps a dataset to a particular range axis. All data will be plotted
     * against axis zero by default, no mapping is required for this case.
     * 
     * @param index
     *            the dataset index (zero-based).
     * @param axisIndex
     *            the axis index.
     * 
     * @see #mapDatasetToDomainAxis(int, int)
     */
    public void mapDatasetToRangeAxis(int index, int axisIndex) {
        List axisIndices = new java.util.ArrayList(1);
        axisIndices.add(new Integer(axisIndex));
        mapDatasetToRangeAxes(index, axisIndices);
    }

    /**
     * Maps the specified dataset to the axes in the list. Note that the
     * conversion of data values into Java2D space is always performed using the
     * first axis in the list.
     * 
     * @param index
     *            the dataset index (zero-based).
     * @param axisIndices
     *            the axis indices (<code>null</code> permitted).
     * 
     * @since JFreeChart 1.0.12
     */
    public void mapDatasetToRangeAxes(int index, List axisIndices) {
        if (index < 0) {
            throw new IllegalArgumentException("Requires 'index' >= 0.");
        }
        checkAxisIndices(axisIndices);
        Integer key = new Integer(index);
        this.datasetToRangeAxesMap.put(key, new ArrayList(axisIndices));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }

    /**
     * This method is used to perform argument checking on the list of axis
     * indices passed to mapDatasetToDomainAxes() and mapDatasetToRangeAxes().
     * 
     * @param indices
     *            the list of indices (<code>null</code> permitted).
     */
    private void checkAxisIndices(List indices) {
        // axisIndices can be:
        // 1. null;
        // 2. non-empty, containing only Integer objects that are unique.
        if (indices == null) {
            return; // OK
        }
        int count = indices.size();
        if (count == 0) {
            throw new IllegalArgumentException("Empty list not permitted.");
        }
        HashSet set = new HashSet();
        for (int i = 0; i < count; i++) {
            Object item = indices.get(i);
            if (!(item instanceof Integer)) {
                throw new IllegalArgumentException(
                        "Indices must be Integer instances.");
            }
            if (set.contains(item)) {
                throw new IllegalArgumentException("Indices must be unique.");
            }
            set.add(item);
        }
    }

    /**
     * Returns the number of renderer slots for this plot.
     * 
     * @return The number of renderer slots.
     * 
     * @since JFreeChart 1.0.11
     */
    public int getRendererCount() {
        return this.renderers.size();
    }

    /**
     * Returns the renderer for the primary dataset.
     * 
     * @return The item renderer (possibly <code>null</code>).
     * 
     * @see #setRenderer(XYItemRenderer)
     */
    public XYItemRenderer getRenderer() {
        return getRenderer(0);
    }

    /**
     * Returns the renderer for a dataset, or <code>null</code>.
     * 
     * @param index
     *            the renderer index.
     * 
     * @return The renderer (possibly <code>null</code>).
     * 
     * @see #setRenderer(int, XYItemRenderer)
     */
    public XYItemRenderer getRenderer(int index) {
        XYItemRenderer result = null;
        if (this.renderers.size() > index) {
            result = (XYItemRenderer) this.renderers.get(index);
        }
        return result;

    }

    /**
     * Sets the renderer for the primary dataset and sends a
     * {@link PlotChangeEvent} to all registered listeners. If the renderer is
     * set to <code>null</code>, no data will be displayed.
     * 
     * @param renderer
     *            the renderer (<code>null</code> permitted).
     * 
     * @see #getRenderer()
     */
    public void setRenderer(XYItemRenderer renderer) {
        setRenderer(0, renderer);
    }

    /**
     * Sets a renderer and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     * 
     * @param index
     *            the index.
     * @param renderer
     *            the renderer.
     * 
     * @see #getRenderer(int)
     */
    public void setRenderer(int index, XYItemRenderer renderer) {
        setRenderer(index, renderer, true);
    }

    /**
     * Sets a renderer and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     * 
     * @param index
     *            the index.
     * @param renderer
     *            the renderer.
     * @param notify
     *            notify listeners?
     * 
     * @see #getRenderer(int)
     */
    public void setRenderer(int index, XYItemRenderer renderer, boolean notify) {
        XYItemRenderer existing = getRenderer(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.renderers.set(index, renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener(this);
        }
        configureDomainAxes();
        configureRangeAxes();
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Sets the renderers for this plot and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param renderers
     *            the renderers (<code>null</code> not permitted).
     */
    public void setRenderers(XYItemRenderer[] renderers) {
        for (int i = 0; i < renderers.length; i++) {
            setRenderer(i, renderers[i], false);
        }
        fireChangeEvent();
    }

    /**
     * Returns the dataset rendering order.
     * 
     * @return The order (never <code>null</code>).
     * 
     * @see #setDatasetRenderingOrder(DatasetRenderingOrder)
     */
    public DatasetRenderingOrder getDatasetRenderingOrder() {
        return this.datasetRenderingOrder;
    }

    /**
     * Sets the rendering order and sends a {@link PlotChangeEvent} to all
     * registered listeners. By default, the plot renders the primary dataset
     * last (so that the primary dataset overlays the secondary datasets). You
     * can reverse this if you want to.
     * 
     * @param order
     *            the rendering order (<code>null</code> not permitted).
     * 
     * @see #getDatasetRenderingOrder()
     */
    public void setDatasetRenderingOrder(DatasetRenderingOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.datasetRenderingOrder = order;
        fireChangeEvent();
    }

    /**
     * Returns the series rendering order.
     * 
     * @return the order (never <code>null</code>).
     * 
     * @see #setSeriesRenderingOrder(SeriesRenderingOrder)
     */
    public SeriesRenderingOrder getSeriesRenderingOrder() {
        return this.seriesRenderingOrder;
    }

    /**
     * Sets the series order and sends a {@link PlotChangeEvent} to all
     * registered listeners. By default, the plot renders the primary series
     * last (so that the primary series appears to be on top). You can reverse
     * this if you want to.
     * 
     * @param order
     *            the rendering order (<code>null</code> not permitted).
     * 
     * @see #getSeriesRenderingOrder()
     */
    public void setSeriesRenderingOrder(SeriesRenderingOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.seriesRenderingOrder = order;
        fireChangeEvent();
    }

    /**
     * Returns the index of the specified renderer, or <code>-1</code> if the
     * renderer is not assigned to this plot.
     * 
     * @param renderer
     *            the renderer (<code>null</code> permitted).
     * 
     * @return The renderer index.
     */
    public int getIndexOf(XYItemRenderer renderer) {
        return this.renderers.indexOf(renderer);
    }

    /**
     * Returns the renderer for the specified dataset. The code first determines
     * the index of the dataset, then checks if there is a renderer with the
     * same index (if not, the method returns renderer(0).
     * 
     * @param dataset
     *            the dataset (<code>null</code> permitted).
     * 
     * @return The renderer (possibly <code>null</code>).
     */
    public XYItemRenderer getRendererForDataset(XYDataset dataset) {
        XYItemRenderer result = null;
        for (int i = 0; i < this.datasets.size(); i++) {
            if (this.datasets.get(i) == dataset) {
                result = (XYItemRenderer) this.renderers.get(i);
                if (result == null) {
                    result = getRenderer();
                }
                break;
            }
        }
        return result;
    }

    /**
     * Returns the weight for this plot when it is used as a subplot within a
     * combined plot.
     * 
     * @return The weight.
     * 
     * @see #setWeight(int)
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Sets the weight for the plot and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     * 
     * @param weight
     *            the weight.
     * 
     * @see #getWeight()
     */
    public void setWeight(int weight) {
        this.weight = weight;
        fireChangeEvent();
    }

    /**
     * Returns <code>true</code> if the domain gridlines are visible, and
     * <code>false<code> otherwise.
     * 
     * @return <code>true</code> or <code>false</code>.
     * 
     * @see #setDomainGridlinesVisible(boolean)
     */
    public boolean isDomainGridlinesVisible() {
        return this.domainGridlinesVisible;
    }

    /**
     * Sets the flag that controls whether or not the domain grid-lines are
     * visible.
     * <p>
     * If the flag value is changed, a {@link PlotChangeEvent} is sent to all
     * registered listeners.
     * 
     * @param visible
     *            the new value of the flag.
     * 
     * @see #isDomainGridlinesVisible()
     */
    public void setDomainGridlinesVisible(boolean visible) {
        if (this.domainGridlinesVisible != visible) {
            this.domainGridlinesVisible = visible;
            fireChangeEvent();
        }
    }

    /**
     * Returns <code>true</code> if the domain minor gridlines are visible, and
     * <code>false<code> otherwise.
     * 
     * @return <code>true</code> or <code>false</code>.
     * 
     * @see #setDomainMinorGridlinesVisible(boolean)
     * 
     * @since JFreeChart 1.0.12
     */
    public boolean isDomainMinorGridlinesVisible() {
        return this.domainMinorGridlinesVisible;
    }

    /**
     * Sets the flag that controls whether or not the domain minor grid-lines
     * are visible.
     * <p>
     * If the flag value is changed, a {@link PlotChangeEvent} is sent to all
     * registered listeners.
     * 
     * @param visible
     *            the new value of the flag.
     * 
     * @see #isDomainMinorGridlinesVisible()
     * 
     * @since JFreeChart 1.0.12
     */
    public void setDomainMinorGridlinesVisible(boolean visible) {
        if (this.domainMinorGridlinesVisible != visible) {
            this.domainMinorGridlinesVisible = visible;
            fireChangeEvent();
        }
    }

    /**
     * Returns the stroke for the grid-lines (if any) plotted against the domain
     * axis.
     * 
     * @return The stroke (never <code>null</code>).
     * 
     * @see #setDomainGridlineStroke(Float stroke)
     */
    public Float getDomainGridlineStroke() {
        return this.domainGridlineStroke;
    }

    /**
     * Sets the stroke for the grid lines plotted against the domain axis, and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param stroke
     *            the stroke (<code>null</code> not permitted).
     * 
     * @throws IllegalArgumentException
     *             if <code>stroke</code> is <code>null</code>.
     * 
     * @see #getDomainGridlineStroke()
     */
    public void setDomainGridlineStroke(Float stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainGridlineStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the stroke for the minor grid-lines (if any) plotted against the
     * domain axis.
     * 
     * @return The stroke (never <code>null</code>).
     * 
     * @see #setDomainMinorGridlineStroke(Float Stroke)
     * 
     * @since JFreeChart 1.0.12
     */

    public Float getDomainMinorGridlineStroke() {
        return this.domainMinorGridlineStroke;
    }

    /**
     * Sets the stroke for the minor grid lines plotted against the domain axis,
     * and sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param stroke
     *            the stroke (<code>null</code> not permitted).
     * 
     * @throws IllegalArgumentException
     *             if <code>stroke</code> is <code>null</code>.
     * 
     * @see #getDomainMinorGridlineStroke()
     * 
     * @since JFreeChart 1.0.12
     */
    public void setDomainMinorGridlineStroke(Float stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainMinorGridlineStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the paint for the grid lines (if any) plotted against the domain
     * axis.
     * 
     * @return The paint type (never <code>null</code>).
     * 
     * @see #setDomainGridlinePaintType(PaintType paintType)
     */
    public PaintType getDomainGridlinePaintType() {
        return this.domainGridlinePaintType;
    }

    /**
     * Sets the paint for the grid lines plotted against the domain axis, and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param paint
     *            the paint (<code>null</code> not permitted).
     * 
     * @throws IllegalArgumentException
     *             if <code>paint</code> is <code>null</code>.
     * 
     * @see #getDomainGridlinePaintType()
     */
    public void setDomainGridlinePaintType(PaintType paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainGridlinePaintType = paint;
        fireChangeEvent();
    }

    /**
     * Returns the paint for the minor grid lines (if any) plotted against the
     * domain axis.
     * 
     * @return The paint type (never <code>null</code>).
     * 
     * @see #setDomainMinorGridlinePaint(PaintType paintType)
     * 
     * @since JFreeChart 1.0.12
     */
    public PaintType getDomainMinorGridlinePaintType() {
        return this.domainMinorGridlinePaint;
    }

    /**
     * Sets the paint for the minor grid lines plotted against the domain axis,
     * and sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param paint
     *            the paint (<code>null</code> not permitted).
     * 
     * @throws IllegalArgumentException
     *             if <code>paint</code> is <code>null</code>.
     * 
     * @see #getDomainMinorGridlinePaintType()
     * 
     * @since JFreeChart 1.0.12
     */
    public void setDomainMinorGridlinePaint(PaintType paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainMinorGridlinePaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns <code>true</code> if the range axis grid is visible, and
     * <code>false<code> otherwise.
     * 
     * @return A boolean.
     * 
     * @see #setRangeGridlinesVisible(boolean)
     */
    public boolean isRangeGridlinesVisible() {
        return this.rangeGridlinesVisible;
    }

    /**
     * Sets the flag that controls whether or not the range axis grid lines are
     * visible.
     * <p>
     * If the flag value is changed, a {@link PlotChangeEvent} is sent to all
     * registered listeners.
     * 
     * @param visible
     *            the new value of the flag.
     * 
     * @see #isRangeGridlinesVisible()
     */
    public void setRangeGridlinesVisible(boolean visible) {
        if (this.rangeGridlinesVisible != visible) {
            this.rangeGridlinesVisible = visible;
            fireChangeEvent();
        }
    }

    /**
     * Returns the stroke for the grid lines (if any) plotted against the range
     * axis.
     * 
     * @return The stroke (never <code>null</code>).
     * 
     * @see #setRangeGridlineStroke(Float Stroke)
     */
    public Float getRangeGridlineStroke() {
        return this.rangeGridlineStroke;
    }

    /**
     * Sets the stroke for the grid lines plotted against the range axis, and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param stroke
     *            the stroke (<code>null</code> not permitted).
     * 
     * @see #getRangeGridlineStroke()
     */
    public void setRangeGridlineStroke(Float stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeGridlineStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the paint for the grid lines (if any) plotted against the range
     * axis.
     * 
     * @return The paint type (never <code>null</code>).
     * 
     * @see #setRangeGridlinePaintType(PaintType paintType)
     */
    public PaintType getRangeGridlinePaintType() {
        return this.rangeGridlinePaintType;
    }

    /**
     * Sets the paint for the grid lines plotted against the range axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param paint
     *            the paint (<code>null</code> not permitted).
     * 
     * @see #getRangeGridlinePaintType()
     */
    public void setRangeGridlinePaintType(PaintType paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeGridlinePaintType = paint;
        fireChangeEvent();
    }

    /**
     * Returns <code>true</code> if the range axis minor grid is visible, and
     * <code>false<code> otherwise.
     * 
     * @return A boolean.
     * 
     * @see #setRangeMinorGridlinesVisible(boolean)
     * 
     * @since JFreeChart 1.0.12
     */
    public boolean isRangeMinorGridlinesVisible() {
        return this.rangeMinorGridlinesVisible;
    }

    /**
     * Sets the flag that controls whether or not the range axis minor grid
     * lines are visible.
     * <p>
     * If the flag value is changed, a {@link PlotChangeEvent} is sent to all
     * registered listeners.
     * 
     * @param visible
     *            the new value of the flag.
     * 
     * @see #isRangeMinorGridlinesVisible()
     * 
     * @since JFreeChart 1.0.12
     */
    public void setRangeMinorGridlinesVisible(boolean visible) {
        if (this.rangeMinorGridlinesVisible != visible) {
            this.rangeMinorGridlinesVisible = visible;
            fireChangeEvent();
        }
    }

    /**
     * Returns the stroke for the minor grid lines (if any) plotted against the
     * range axis.
     * 
     * @return The stroke (never <code>null</code>).
     * 
     * @see #setRangeMinorGridlineStroke(Float Stroke)
     * 
     * @since JFreeChart 1.0.12
     */
    public Float getRangeMinorGridlineStroke() {
        return this.rangeMinorGridlineStroke;
    }

    /**
     * Sets the stroke for the minor grid lines plotted against the range axis,
     * and sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param stroke
     *            the stroke (<code>null</code> not permitted).
     * 
     * @see #getRangeMinorGridlineStroke()
     * 
     * @since JFreeChart 1.0.12
     */
    public void setRangeMinorGridlineStroke(Float stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeMinorGridlineStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the paint for the minor grid lines (if any) plotted against the
     * range axis.
     * 
     * @return The paint type (never <code>null</code>).
     * 
     * @see #setRangeMinorGridlinePaint(PaintType paintType)
     * 
     * @since JFreeChart 1.0.12
     */
    public PaintType getRangeMinorGridlinePaintType() {
        return this.rangeMinorGridlinePaint;
    }

    /**
     * Sets the paint for the minor grid lines plotted against the range axis
     * and sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param paint
     *            the paint (<code>null</code> not permitted).
     * 
     * @see #getRangeMinorGridlinePaintType()
     * 
     * @since JFreeChart 1.0.12
     */
    public void setRangeMinorGridlinePaint(PaintType paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeMinorGridlinePaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns a flag that controls whether or not a zero baseline is displayed
     * for the domain axis.
     * 
     * @return A boolean.
     * 
     * @since JFreeChart 1.0.5
     * 
     * @see #setDomainZeroBaselineVisible(boolean)
     */
    public boolean isDomainZeroBaselineVisible() {
        return this.domainZeroBaselineVisible;
    }

    /**
     * Sets the flag that controls whether or not the zero baseline is displayed
     * for the domain axis, and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     * 
     * @param visible
     *            the flag.
     * 
     * @since JFreeChart 1.0.5
     * 
     * @see #isDomainZeroBaselineVisible()
     */
    public void setDomainZeroBaselineVisible(boolean visible) {
        this.domainZeroBaselineVisible = visible;
        fireChangeEvent();
    }

    /**
     * Returns the stroke used for the zero baseline against the domain axis.
     * 
     * @return The stroke (never <code>null</code>).
     * 
     * @since JFreeChart 1.0.5
     * 
     * @see #setDomainZeroBaselineStroke(Float Stroke)
     */
    public Float getDomainZeroBaselineStroke() {
        return this.domainZeroBaselineStroke;
    }

    /**
     * Sets the stroke for the zero baseline for the domain axis, and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param stroke
     *            the stroke (<code>null</code> not permitted).
     * 
     * @since JFreeChart 1.0.5
     * 
     * @see #getRangeZeroBaselineStroke()
     */
    public void setDomainZeroBaselineStroke(Float stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainZeroBaselineStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the paint for the zero baseline (if any) plotted against the
     * domain axis.
     * 
     * @since JFreeChart 1.0.5
     * 
     * @return The paint type (never <code>null</code>).
     * 
     * @see #setDomainZeroBaselinePaintType(PaintType paintType)
     */
    public PaintType getDomainZeroBaselinePaintType() {
        return this.domainZeroBaselinePaint;
    }

    /**
     * Sets the paint for the zero baseline plotted against the domain axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param paint
     *            the paint (<code>null</code> not permitted).
     * 
     * @since JFreeChart 1.0.5
     * 
     * @see #getDomainZeroBaselinePaintType()
     */
    public void setDomainZeroBaselinePaintType(PaintType paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainZeroBaselinePaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns a flag that controls whether or not a zero baseline is displayed
     * for the range axis.
     * 
     * @return A boolean.
     * 
     * @see #setRangeZeroBaselineVisible(boolean)
     */
    public boolean isRangeZeroBaselineVisible() {
        return this.rangeZeroBaselineVisible;
    }

    /**
     * Sets the flag that controls whether or not the zero baseline is displayed
     * for the range axis, and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     * 
     * @param visible
     *            the flag.
     * 
     * @see #isRangeZeroBaselineVisible()
     */
    public void setRangeZeroBaselineVisible(boolean visible) {
        this.rangeZeroBaselineVisible = visible;
        fireChangeEvent();
    }

    /**
     * Returns the stroke used for the zero baseline against the range axis.
     * 
     * @return The stroke (never <code>null</code>).
     * 
     * @see #setRangeZeroBaselineStroke(Float Stroke)
     */
    public Float getRangeZeroBaselineStroke() {
        return this.rangeZeroBaselineStroke;
    }

    /**
     * Sets the stroke for the zero baseline for the range axis, and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param stroke
     *            the stroke (<code>null</code> not permitted).
     * 
     * @see #getRangeZeroBaselineStroke()
     */
    public void setRangeZeroBaselineStroke(Float stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeZeroBaselineStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the paint for the zero baseline (if any) plotted against the
     * range axis.
     * 
     * @return The paint type (never <code>null</code>).
     * 
     * @see #setRangeZeroBaselinePaintType(PaintType paintType)
     */
    public PaintType getRangeZeroBaselinePaintType() {
        return this.rangeZeroBaselinePaint;
    }

    /**
     * Sets the paint for the zero baseline plotted against the range axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param paint
     *            the paint (<code>null</code> not permitted).
     * 
     * @see #getRangeZeroBaselinePaintType()
     */
    public void setRangeZeroBaselinePaintType(PaintType paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeZeroBaselinePaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns the paint used for the domain tick bands. If this is
     * <code>null</code>, no tick bands will be drawn.
     * 
     * @return The paint type (possibly <code>null</code>).
     * 
     * @see #setDomainTickBandPaint(PaintType paintType)
     */
    public PaintType getDomainTickBandPaintType() {
        return this.domainTickBandPaint;
    }

    /**
     * Sets the paint for the domain tick bands.
     * 
     * @param paint
     *            the paint (<code>null</code> permitted).
     * 
     * @see #getDomainTickBandPaintType()
     */
    public void setDomainTickBandPaint(PaintType paint) {
        this.domainTickBandPaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns the paint used for the range tick bands. If this is
     * <code>null</code>, no tick bands will be drawn.
     * 
     * @return The paint type (possibly <code>null</code>).
     * 
     * @see #setRangeTickBandPaintType(PaintType paintType)
     */
    public PaintType getRangeTickBandPaintType() {
        return this.rangeTickBandPaint;
    }

    /**
     * Sets the paint for the range tick bands.
     * 
     * @param paint
     *            the paint (<code>null</code> permitted).
     * 
     * @see #getRangeTickBandPaintType()
     */
    public void setRangeTickBandPaintType(PaintType paint) {
        this.rangeTickBandPaint = paint;
        fireChangeEvent();
    }

    /**
     * Returns the origin for the quadrants that can be displayed on the plot.
     * This defaults to (0, 0).
     * 
     * @return The origin point (never <code>null</code>).
     * 
     * @see #setQuadrantOrigin(PointF)
     */
    public PointF getQuadrantOrigin() {
        return this.quadrantOrigin;
    }

    /**
     * Sets the quadrant origin and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     * 
     * @param origin
     *            the origin (<code>null</code> not permitted).
     * 
     * @see #getQuadrantOrigin()
     */
    public void setQuadrantOrigin(PointF origin) {
        if (origin == null) {
            throw new IllegalArgumentException("Null 'origin' argument.");
        }
        this.quadrantOrigin = origin;
        fireChangeEvent();
    }

    /**
     * Returns the paint used for the specified quadrant.
     * 
     * @param index
     *            the quadrant index (0-3).
     * 
     * @return The paint type (possibly <code>null</code>).
     * 
     * @see #setQuadrantPaint(int, PaintType)
     */
    public PaintType getQuadrantPaintType(int index) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("The index value (" + index
                    + ") should be in the range 0 to 3.");
        }
        return this.quadrantPaint[index];
    }

    /**
     * Sets the paint used for the specified quadrant and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param index
     *            the quadrant index (0-3).
     * @param paint
     *            the paint (<code>null</code> permitted).
     * 
     * @see #getQuadrantPaintType(int)
     */
    public void setQuadrantPaint(int index, PaintType paint) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("The index value (" + index
                    + ") should be in the range 0 to 3.");
        }
        this.quadrantPaint[index] = paint;
        fireChangeEvent();
    }

    /**
     * Adds a marker for the domain axis and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to the range axis, however this is entirely up to the renderer.
     * 
     * @param marker
     *            the marker (<code>null</code> not permitted).
     * 
     * @see #addDomainMarker(Marker, Layer)
     * @see #clearDomainMarkers()
     */
    public void addDomainMarker(Marker marker) {
        // defer argument checking...
        addDomainMarker(marker, Layer.FOREGROUND);
    }

    /**
     * Adds a marker for the domain axis in the specified layer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to the range axis, however this is entirely up to the renderer.
     * 
     * @param marker
     *            the marker (<code>null</code> not permitted).
     * @param layer
     *            the layer (foreground or background).
     * 
     * @see #addDomainMarker(int, Marker, Layer)
     */
    public void addDomainMarker(Marker marker, Layer layer) {
        addDomainMarker(0, marker, layer);
    }

    /**
     * Clears all the (foreground and background) domain markers and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @see #addDomainMarker(int, Marker, Layer)
     */
    public void clearDomainMarkers() {
        if (this.backgroundDomainMarkers != null) {
            Set keys = this.backgroundDomainMarkers.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                Integer key = (Integer) iterator.next();
                clearDomainMarkers(key.intValue());
            }
            this.backgroundDomainMarkers.clear();
        }
        if (this.foregroundDomainMarkers != null) {
            Set keys = this.foregroundDomainMarkers.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                Integer key = (Integer) iterator.next();
                clearDomainMarkers(key.intValue());
            }
            this.foregroundDomainMarkers.clear();
        }
        fireChangeEvent();
    }

    /**
     * Clears the (foreground and background) domain markers for a particular
     * renderer.
     * 
     * @param index
     *            the renderer index.
     * 
     * @see #clearRangeMarkers(int)
     */
    public void clearDomainMarkers(int index) {
        Integer key = new Integer(index);
        if (this.backgroundDomainMarkers != null) {
            Collection markers = (Collection) this.backgroundDomainMarkers
                    .get(key);
            if (markers != null) {
                Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    Marker m = (Marker) iterator.next();
                    m.removeChangeListener(this);
                }
                markers.clear();
            }
        }
        if (this.foregroundRangeMarkers != null) {
            Collection markers = (Collection) this.foregroundDomainMarkers
                    .get(key);
            if (markers != null) {
                Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    Marker m = (Marker) iterator.next();
                    m.removeChangeListener(this);
                }
                markers.clear();
            }
        }
        fireChangeEvent();
    }

    /**
     * Adds a marker for a specific dataset/renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to the domain axis (that the renderer is mapped to), however this is
     * entirely up to the renderer.
     * 
     * @param index
     *            the dataset/renderer index.
     * @param marker
     *            the marker.
     * @param layer
     *            the layer (foreground or background).
     * 
     * @see #clearDomainMarkers(int)
     * @see #addRangeMarker(int, Marker, Layer)
     */
    public void addDomainMarker(int index, Marker marker, Layer layer) {
        addDomainMarker(index, marker, layer, true);
    }

    /**
     * Adds a marker for a specific dataset/renderer and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to the domain axis (that the renderer is mapped to), however this is
     * entirely up to the renderer.
     * 
     * @param index
     *            the dataset/renderer index.
     * @param marker
     *            the marker.
     * @param layer
     *            the layer (foreground or background).
     * @param notify
     *            notify listeners?
     * 
     * @since JFreeChart 1.0.10
     */
    public void addDomainMarker(int index, Marker marker, Layer layer,
            boolean notify) {
        if (marker == null) {
            throw new IllegalArgumentException("Null 'marker' not permitted.");
        }
        if (layer == null) {
            throw new IllegalArgumentException("Null 'layer' not permitted.");
        }
        Collection markers;
        if (layer == Layer.FOREGROUND) {
            markers = (Collection) this.foregroundDomainMarkers
                    .get(new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.foregroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        } else if (layer == Layer.BACKGROUND) {
            markers = (Collection) this.backgroundDomainMarkers
                    .get(new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.backgroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
         marker.addChangeListener(this);
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Removes a marker for the domain axis and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     * 
     * @param marker
     *            the marker.
     * 
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     * 
     * @since JFreeChart 1.0.7
     */
    public boolean removeDomainMarker(Marker marker) {
        return removeDomainMarker(marker, Layer.FOREGROUND);
    }

    /**
     * Removes a marker for the domain axis in the specified layer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param marker
     *            the marker (<code>null</code> not permitted).
     * @param layer
     *            the layer (foreground or background).
     * 
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     * 
     * @since JFreeChart 1.0.7
     */
    public boolean removeDomainMarker(Marker marker, Layer layer) {
        return removeDomainMarker(0, marker, layer);
    }

    /**
     * Removes a marker for a specific dataset/renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param index
     *            the dataset/renderer index.
     * @param marker
     *            the marker.
     * @param layer
     *            the layer (foreground or background).
     * 
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     * 
     * @since JFreeChart 1.0.7
     */
    public boolean removeDomainMarker(int index, Marker marker, Layer layer) {
        return removeDomainMarker(index, marker, layer, true);
    }

    /**
     * Removes a marker for a specific dataset/renderer and, if requested, sends
     * a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param index
     *            the dataset/renderer index.
     * @param marker
     *            the marker.
     * @param layer
     *            the layer (foreground or background).
     * @param notify
     *            notify listeners?
     * 
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     * 
     * @since JFreeChart 1.0.10
     */
    public boolean removeDomainMarker(int index, Marker marker, Layer layer,
            boolean notify) {
        ArrayList markers;
        if (layer == Layer.FOREGROUND) {
            markers = (ArrayList) this.foregroundDomainMarkers.get(new Integer(
                    index));
        } else {
            markers = (ArrayList) this.backgroundDomainMarkers.get(new Integer(
                    index));
        }
        if (markers == null) {
            return false;
        }
        boolean removed = markers.remove(marker);
        if (removed && notify) {
            fireChangeEvent();
        }
        return removed;
    }

    /**
     * Adds a marker for the range axis and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to the range axis, however this is entirely up to the renderer.
     * 
     * @param marker
     *            the marker (<code>null</code> not permitted).
     * 
     * @see #addRangeMarker(Marker, Layer)
     */
    public void addRangeMarker(Marker marker) {
        addRangeMarker(marker, Layer.FOREGROUND);
    }

    /**
     * Adds a marker for the range axis in the specified layer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to the range axis, however this is entirely up to the renderer.
     * 
     * @param marker
     *            the marker (<code>null</code> not permitted).
     * @param layer
     *            the layer (foreground or background).
     * 
     * @see #addRangeMarker(int, Marker, Layer)
     */
    public void addRangeMarker(Marker marker, Layer layer) {
        addRangeMarker(0, marker, layer);
    }

    /**
     * Clears all the range markers and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     * 
     * @see #clearRangeMarkers()
     */
    public void clearRangeMarkers() {
        if (this.backgroundRangeMarkers != null) {
            Set keys = this.backgroundRangeMarkers.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                Integer key = (Integer) iterator.next();
                clearRangeMarkers(key.intValue());
            }
            this.backgroundRangeMarkers.clear();
        }
        if (this.foregroundRangeMarkers != null) {
            Set keys = this.foregroundRangeMarkers.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                Integer key = (Integer) iterator.next();
                clearRangeMarkers(key.intValue());
            }
            this.foregroundRangeMarkers.clear();
        }
        fireChangeEvent();
    }

    /**
     * Adds a marker for a specific dataset/renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to the range axis, however this is entirely up to the renderer.
     * 
     * @param index
     *            the dataset/renderer index.
     * @param marker
     *            the marker.
     * @param layer
     *            the layer (foreground or background).
     * 
     * @see #clearRangeMarkers(int)
     * @see #addDomainMarker(int, Marker, Layer)
     */
    public void addRangeMarker(int index, Marker marker, Layer layer) {
        addRangeMarker(index, marker, layer, true);
    }

    /**
     * Adds a marker for a specific dataset/renderer and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to the range axis, however this is entirely up to the renderer.
     * 
     * @param index
     *            the dataset/renderer index.
     * @param marker
     *            the marker.
     * @param layer
     *            the layer (foreground or background).
     * @param notify
     *            notify listeners?
     * 
     * @since JFreeChart 1.0.10
     */
    public void addRangeMarker(int index, Marker marker, Layer layer,
            boolean notify) {
        Collection markers;
        if (layer == Layer.FOREGROUND) {
            markers = (Collection) this.foregroundRangeMarkers.get(new Integer(
                    index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.foregroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        } else if (layer == Layer.BACKGROUND) {
            markers = (Collection) this.backgroundRangeMarkers.get(new Integer(
                    index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.backgroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        marker.addChangeListener(this);
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Clears the (foreground and background) range markers for a particular
     * renderer.
     * 
     * @param index
     *            the renderer index.
     */
    public void clearRangeMarkers(int index) {
        Integer key = new Integer(index);
        if (this.backgroundRangeMarkers != null) {
            Collection markers = (Collection) this.backgroundRangeMarkers
                    .get(key);
            if (markers != null) {
                Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    Marker m = (Marker) iterator.next();
                    m.removeChangeListener(this);
                }
                markers.clear();
            }
        }
        if (this.foregroundRangeMarkers != null) {
            Collection markers = (Collection) this.foregroundRangeMarkers
                    .get(key);
            if (markers != null) {
                Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    Marker m = (Marker) iterator.next();
                    m.removeChangeListener(this);
                }
                markers.clear();
            }
        }
        fireChangeEvent();
    }

    /**
     * Removes a marker for the range axis and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     * 
     * @param marker
     *            the marker.
     * 
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     * 
     * @since JFreeChart 1.0.7
     */
    public boolean removeRangeMarker(Marker marker) {
        return removeRangeMarker(marker, Layer.FOREGROUND);
    }

    /**
     * Removes a marker for the range axis in the specified layer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param marker
     *            the marker (<code>null</code> not permitted).
     * @param layer
     *            the layer (foreground or background).
     * 
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     * 
     * @since JFreeChart 1.0.7
     */
    public boolean removeRangeMarker(Marker marker, Layer layer) {
        return removeRangeMarker(0, marker, layer);
    }

    /**
     * Removes a marker for a specific dataset/renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param index
     *            the dataset/renderer index.
     * @param marker
     *            the marker.
     * @param layer
     *            the layer (foreground or background).
     * 
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     * 
     * @since JFreeChart 1.0.7
     */
    public boolean removeRangeMarker(int index, Marker marker, Layer layer) {
        return removeRangeMarker(index, marker, layer, true);
    }

    /**
     * Removes a marker for a specific dataset/renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param index
     *            the dataset/renderer index.
     * @param marker
     *            the marker.
     * @param layer
     *            the layer (foreground or background).
     * @param notify
     *            notify listeners?
     * 
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     * 
     * @since JFreeChart 1.0.10
     */
    public boolean removeRangeMarker(int index, Marker marker, Layer layer,
            boolean notify) {
        if (marker == null) {
            throw new IllegalArgumentException("Null 'marker' argument.");
        }
        ArrayList markers;
        if (layer == Layer.FOREGROUND) {
            markers = (ArrayList) this.foregroundRangeMarkers.get(new Integer(
                    index));
        } else {
            markers = (ArrayList) this.backgroundRangeMarkers.get(new Integer(
                    index));
        }
        if (markers == null) {
            return false;
        }
        boolean removed = markers.remove(marker);
        if (removed && notify) {
            fireChangeEvent();
        }
        return removed;
    }

    /**
     * Adds an annotation to the plot and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     * 
     * @param annotation
     *            the annotation (<code>null</code> not permitted).
     * 
     * @see #getAnnotations()
     * @see #removeAnnotation(XYAnnotation)
     */
    public void addAnnotation(XYAnnotation annotation) {
        addAnnotation(annotation, true);
    }

    /**
     * Adds an annotation to the plot and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param annotation
     *            the annotation (<code>null</code> not permitted).
     * @param notify
     *            notify listeners?
     * 
     * @since JFreeChart 1.0.10
     */
    public void addAnnotation(XYAnnotation annotation, boolean notify) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(annotation);
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Removes an annotation from the plot and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     * 
     * @param annotation
     *            the annotation (<code>null</code> not permitted).
     * 
     * @return A boolean (indicates whether or not the annotation was removed).
     * 
     * @see #addAnnotation(XYAnnotation)
     * @see #getAnnotations()
     */
    public boolean removeAnnotation(XYAnnotation annotation) {
        return removeAnnotation(annotation, true);
    }

    /**
     * Removes an annotation from the plot and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     * 
     * @param annotation
     *            the annotation (<code>null</code> not permitted).
     * @param notify
     *            notify listeners?
     * 
     * @return A boolean (indicates whether or not the annotation was removed).
     * 
     * @since JFreeChart 1.0.10
     */
    public boolean removeAnnotation(XYAnnotation annotation, boolean notify) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        boolean removed = this.annotations.remove(annotation);
        if (removed && notify) {
            fireChangeEvent();
        }
        return removed;
    }

    /**
     * Returns the list of annotations.
     * 
     * @return The list of annotations.
     * 
     * @since JFreeChart 1.0.1
     * 
     * @see #addAnnotation(XYAnnotation)
     */
    public List getAnnotations() {
        return new ArrayList(this.annotations);
    }

    /**
     * Clears all the annotations and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     * 
     * @see #addAnnotation(XYAnnotation)
     */
    public void clearAnnotations() {
        this.annotations.clear();
        fireChangeEvent();
    }

    /**
     * Calculates the space required for all the axes in the plot.
     * 
     * @param canvas
     *            the graphics device.
     * @param plotArea
     *            the plot area.
     * 
     * @return The required space.
     */
    protected AxisSpace calculateAxisSpace(Canvas canvas, RectShape plotArea) {
        AxisSpace space = new AxisSpace();
        space = calculateRangeAxisSpace(canvas, plotArea, space);
        RectShape revPlotArea = space.shrink(plotArea, null);
        space = calculateDomainAxisSpace(canvas, revPlotArea, space);
        return space;
    }

    /**
     * Calculates the space required for the domain axis/axes.
     * 
     * @param canvas
     *            the graphics device.
     * @param plotArea
     *            the plot area.
     * @param space
     *            a carrier for the result (<code>null</code> permitted).
     * 
     * @return The required space.
     */
    protected AxisSpace calculateDomainAxisSpace(Canvas canvas,
            RectShape plotArea, AxisSpace space) {

        if (space == null) {
            space = new AxisSpace();
        }

        // reserve some space for the domain axis...
        if (this.fixedDomainAxisSpace != null) {
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                space.ensureAtLeast(this.fixedDomainAxisSpace.getLeft(),
                        RectangleEdge.LEFT);
                space.ensureAtLeast(this.fixedDomainAxisSpace.getRight(),
                        RectangleEdge.RIGHT);
            } else if (this.orientation == PlotOrientation.VERTICAL) {
                space.ensureAtLeast(this.fixedDomainAxisSpace.getTop(),
                        RectangleEdge.TOP);
                space.ensureAtLeast(this.fixedDomainAxisSpace.getBottom(),
                        RectangleEdge.BOTTOM);
            }
        } else {
            // reserve space for the domain axes...
            for (int i = 0; i < this.domainAxes.size(); i++) {
                Axis axis = (Axis) this.domainAxes.get(i);
                if (axis != null) {
                    RectangleEdge edge = getDomainAxisEdge(i);
                    space = axis.reserveSpace(canvas, this, plotArea, edge, space);
                }
            }
        }

        return space;

    }

    /**
     * Calculates the space required for the range axis/axes.
     * 
     * @param canvas
     *            the graphics device.
     * @param plotArea
     *            the plot area.
     * @param space
     *            a carrier for the result (<code>null</code> permitted).
     * 
     * @return The required space.
     */
    protected AxisSpace calculateRangeAxisSpace(Canvas canvas,
            RectShape plotArea, AxisSpace space) {

        if (space == null) {
            space = new AxisSpace();
        }

        // reserve some space for the range axis...
        if (this.fixedRangeAxisSpace != null) {
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                space.ensureAtLeast(this.fixedRangeAxisSpace.getTop(),
                        RectangleEdge.TOP);
                space.ensureAtLeast(this.fixedRangeAxisSpace.getBottom(),
                        RectangleEdge.BOTTOM);
            } else if (this.orientation == PlotOrientation.VERTICAL) {
                space.ensureAtLeast(this.fixedRangeAxisSpace.getLeft(),
                        RectangleEdge.LEFT);
                space.ensureAtLeast(this.fixedRangeAxisSpace.getRight(),
                        RectangleEdge.RIGHT);
            }
        } else {
            // reserve space for the range axes...
            for (int i = 0; i < this.rangeAxes.size(); i++) {
                Axis axis = (Axis) this.rangeAxes.get(i);
                if (axis != null) {
                    RectangleEdge edge = getRangeAxisEdge(i);
                    space = axis.reserveSpace(canvas, this, plotArea, edge, space);
                }
            }
        }
        return space;

    }

    /**
     * Draws the plot within the specified area on a graphics device.
     * 
     * @param canvas
     *            the graphics device.
     * @param area
     *            the plot area (in Java2D space).
     * @param anchor
     *            an anchor point in Java2D space (<code>null</code> permitted).
     * @param parentState
     *            the state from the parent plot, if there is one (
     *            <code>null</code> permitted).
     * @param info
     *            collects chart drawing information (<code>null</code>
     *            permitted).
     */
    public void draw(Canvas canvas, RectShape area, PointF anchor,
            PlotState parentState, PlotRenderingInfo info) {

        // if the plot area is too small, just return...
        boolean b1 = (area.getWidth() <= MINIMUM_WIDTH_TO_DRAW);
        boolean b2 = (area.getHeight() <= MINIMUM_HEIGHT_TO_DRAW);
        if (b1 || b2) {
            return;
        }

        // record the plot area...
        if (info != null) {
            info.setPlotArea(area);
        }

        // adjust the drawing area for the plot insets (if any)...
        RectangleInsets insets = getInsets();
        insets.trim(area);

        AxisSpace space = calculateAxisSpace(canvas, area);
        RectShape dataArea = space.shrink(area, null);
        this.axisOffset.trim(dataArea);
        createAndAddEntity((RectShape) dataArea.clone(), info, null, null);
        if (info != null) {
            info.setDataArea(dataArea);
        }

        // draw the plot background and axes...
        drawBackground(canvas, dataArea);
        Map axisStateMap = drawAxes(canvas, area, dataArea, info);

        PlotOrientation orient = getOrientation();

        // the anchor point is typically the point where the mouse last
        // clicked - the crosshairs will be driven off this point...
        if (anchor != null && !dataArea.contains(anchor)) {
            anchor = null;
        }
        CrosshairState crosshairState = new CrosshairState();
        crosshairState.setCrosshairDistance(Double.POSITIVE_INFINITY);
        crosshairState.setAnchor(anchor);

        crosshairState.setAnchorX(Double.NaN);
        crosshairState.setAnchorY(Double.NaN);
        if (anchor != null) {
            ValueAxis domainAxis = getDomainAxis();
            if (domainAxis != null) {
                double x;
                if (orient == PlotOrientation.VERTICAL) {
                    x = domainAxis.java2DToValue(anchor.x, dataArea,
                            getDomainAxisEdge());
                } else {
                    x = domainAxis.java2DToValue(anchor.y, dataArea,
                            getDomainAxisEdge());
                }
                crosshairState.setAnchorX(x);
            }
            ValueAxis rangeAxis = getRangeAxis();
            if (rangeAxis != null) {
                double y;
                if (orient == PlotOrientation.VERTICAL) {
                    y = rangeAxis.java2DToValue(anchor.y, dataArea,
                            getRangeAxisEdge());
                } else {
                    y = rangeAxis.java2DToValue(anchor.x, dataArea,
                            getRangeAxisEdge());
                }
                crosshairState.setAnchorY(y);
            }
        }
        crosshairState.setCrosshairX(getDomainCrosshairValue());
        crosshairState.setCrosshairY(getRangeCrosshairValue());

        canvas.save();
        canvas.clipRect((float) dataArea.getMinX(), (float) dataArea.getMinY(),
                (float) dataArea.getMaxX(), (float) dataArea.getMaxY());

        AxisState domainAxisState = (AxisState) axisStateMap
                .get(getDomainAxis());
        if (domainAxisState == null) {
            if (parentState != null) {
                domainAxisState = (AxisState) parentState.getSharedAxisStates()
                        .get(getDomainAxis());
            }
        }

        AxisState rangeAxisState = (AxisState) axisStateMap.get(getRangeAxis());
        if (rangeAxisState == null) {
            if (parentState != null) {
                rangeAxisState = (AxisState) parentState.getSharedAxisStates()
                        .get(getRangeAxis());
            }
        }
        if (domainAxisState != null) {
            drawDomainTickBands(canvas, dataArea, domainAxisState.getTicks());
        }
        if (rangeAxisState != null) {
            drawRangeTickBands(canvas, dataArea, rangeAxisState.getTicks());
        }
        if (domainAxisState != null) {
            drawDomainGridlines(canvas, dataArea, domainAxisState.getTicks());
            drawZeroDomainBaseline(canvas, dataArea);
        }
        if (rangeAxisState != null) {
            drawRangeGridlines(canvas, dataArea, rangeAxisState.getTicks());
            drawZeroRangeBaseline(canvas, dataArea);
        }

        // draw the markers that are associated with a specific renderer...
        for (int i = 0; i < this.renderers.size(); i++) {
            drawDomainMarkers(canvas, dataArea, i, Layer.BACKGROUND);
        }
        for (int i = 0; i < this.renderers.size(); i++) {
            drawRangeMarkers(canvas, dataArea, i, Layer.BACKGROUND);
        }

        // now draw annotations and render data items...
        boolean foundData = false;
        DatasetRenderingOrder order = getDatasetRenderingOrder();
        if (order == DatasetRenderingOrder.FORWARD) {

            // draw background annotations
            int rendererCount = this.renderers.size();
            for (int i = 0; i < rendererCount; i++) {
                XYItemRenderer r = getRenderer(i);
                if (r != null) {
                    ValueAxis domainAxis = getDomainAxisForDataset(i);
                    ValueAxis rangeAxis = getRangeAxisForDataset(i);
                    r.drawAnnotations(canvas, dataArea, domainAxis, rangeAxis,
                            Layer.BACKGROUND, info);
                }
            }

            // render data items...
            for (int i = 0; i < getDatasetCount(); i++) {
                foundData = render(canvas, dataArea, i, info, crosshairState)
                        || foundData;
            }

            // draw foreground annotations
            for (int i = 0; i < rendererCount; i++) {
                XYItemRenderer r = getRenderer(i);
                if (r != null) {
                    ValueAxis domainAxis = getDomainAxisForDataset(i);
                    ValueAxis rangeAxis = getRangeAxisForDataset(i);
                    r.drawAnnotations(canvas, dataArea, domainAxis, rangeAxis,
                            Layer.FOREGROUND, info);
                }
            }

        } else if (order == DatasetRenderingOrder.REVERSE) {

            // draw background annotations
            int rendererCount = this.renderers.size();
            for (int i = rendererCount - 1; i >= 0; i--) {
                XYItemRenderer r = getRenderer(i);
                if (i >= getDatasetCount()) { // we need the dataset to make
                    continue; // a link to the axes
                }
                if (r != null) {
                    ValueAxis domainAxis = getDomainAxisForDataset(i);
                    ValueAxis rangeAxis = getRangeAxisForDataset(i);
                    r.drawAnnotations(canvas, dataArea, domainAxis, rangeAxis,
                            Layer.BACKGROUND, info);
                }
            }

            for (int i = getDatasetCount() - 1; i >= 0; i--) {
                foundData = render(canvas, dataArea, i, info, crosshairState)
                        || foundData;
            }

            // draw foreground annotations
            for (int i = rendererCount - 1; i >= 0; i--) {
                XYItemRenderer r = getRenderer(i);
                if (i >= getDatasetCount()) { // we need the dataset to make
                    continue; // a link to the axes
                }
                if (r != null) {
                    ValueAxis domainAxis = getDomainAxisForDataset(i);
                    ValueAxis rangeAxis = getRangeAxisForDataset(i);
                    r.drawAnnotations(canvas, dataArea, domainAxis, rangeAxis,
                            Layer.FOREGROUND, info);
                }
            }

        }

        // draw domain crosshair if required...
        int xAxisIndex = crosshairState.getDomainAxisIndex();
        ValueAxis xAxis = getDomainAxis(xAxisIndex);
        RectangleEdge xAxisEdge = getDomainAxisEdge(xAxisIndex);
        if (!this.domainCrosshairLockedOnData && anchor != null) {
            double xx;
            if (orient == PlotOrientation.VERTICAL) {
                xx = xAxis.java2DToValue(anchor.x, dataArea, xAxisEdge);
            } else {
                xx = xAxis.java2DToValue(anchor.y, dataArea, xAxisEdge);
            }
            crosshairState.setCrosshairX(xx);
        }
        setDomainCrosshairValue(crosshairState.getCrosshairX(), false);
        if (isDomainCrosshairVisible()) {
            double x = getDomainCrosshairValue();
            PaintType paintType = getDomainCrosshairPaintType();
            int oldAlpha = paintType.getAlpha();
            paintType.setAlpha(getForegroundAlpha());
            Float stroke = getDomainCrosshairStroke();
            PathEffect pathEffect = getDomainCrosshairEffect();
            drawDomainCrosshair(canvas, dataArea, orient, x, xAxis, stroke, paintType, pathEffect);
            paintType.setAlpha(oldAlpha);
        }

        // draw range crosshair if required...
        int yAxisIndex = crosshairState.getRangeAxisIndex();
        ValueAxis yAxis = getRangeAxis(yAxisIndex);
        RectangleEdge yAxisEdge = getRangeAxisEdge(yAxisIndex);
        if (!this.rangeCrosshairLockedOnData && anchor != null) {
            double yy;
            if (orient == PlotOrientation.VERTICAL) {
                yy = yAxis.java2DToValue(anchor.y, dataArea, yAxisEdge);
            } else {
                yy = yAxis.java2DToValue(anchor.x, dataArea, yAxisEdge);
            }
            crosshairState.setCrosshairY(yy);
        }
        setRangeCrosshairValue(crosshairState.getCrosshairY(), false);
        if (isRangeCrosshairVisible()) {
            double y = getRangeCrosshairValue();
            PaintType paintType = getRangeCrosshairPaintType();
            int oldAlpha = paintType.getAlpha();
            paintType.setAlpha(getForegroundAlpha());
            Float stroke = getRangeCrosshairStroke();
            PathEffect effect = getRangeCrosshairEffect();
            drawRangeCrosshair(canvas, dataArea, orient, y, yAxis, stroke, paintType, effect);
            paintType.setAlpha(oldAlpha);
        }

        if (!foundData) {
            drawNoDataMessage(canvas, dataArea);
        }

        for (int i = 0; i < this.renderers.size(); i++) {
            drawDomainMarkers(canvas, dataArea, i, Layer.FOREGROUND);
        }
        for (int i = 0; i < this.renderers.size(); i++) {
            drawRangeMarkers(canvas, dataArea, i, Layer.FOREGROUND);
        }

        drawAnnotations(canvas, dataArea, info);
        canvas.restore();
        drawOutline(canvas, dataArea);

    }

    /**
     * Draws the background for the plot.
     * 
     * @param canvas
     *            the graphics device.
     * @param area
     *            the area.
     */
    public void drawBackground(Canvas canvas, RectShape area) {
        fillBackground(canvas, area, this.orientation);
        drawQuadrants(canvas, area);
        drawBackgroundImage(canvas, area);
    }

    /**
     * Draws the quadrants.
     * 
     * @param canvas
     *            the graphics device.
     * @param area
     *            the area.
     * 
     * @see #setQuadrantOrigin(PointF)
     * @see #setQuadrantPaint(int, PaintType)
     */
    protected void drawQuadrants(Canvas canvas, RectShape area) {
        // 0 | 1
        // --+--
        // 2 | 3
        boolean somethingToDraw = false;

        ValueAxis xAxis = getDomainAxis();
        if (xAxis == null) { // we can't draw quadrants without a valid x-axis
            return;
        }
        double x = xAxis.getRange().constrain(this.quadrantOrigin.x);
        double xx = xAxis.valueToJava2D(x, area, getDomainAxisEdge());

        ValueAxis yAxis = getRangeAxis();
        if (yAxis == null) { // we can't draw quadrants without a valid y-axis
            return;
        }
        double y = yAxis.getRange().constrain(this.quadrantOrigin.y);
        double yy = yAxis.valueToJava2D(y, area, getRangeAxisEdge());

        double xmin = xAxis.getLowerBound();
        double xxmin = xAxis.valueToJava2D(xmin, area, getDomainAxisEdge());

        double xmax = xAxis.getUpperBound();
        double xxmax = xAxis.valueToJava2D(xmax, area, getDomainAxisEdge());

        double ymin = yAxis.getLowerBound();
        double yymin = yAxis.valueToJava2D(ymin, area, getRangeAxisEdge());

        double ymax = yAxis.getUpperBound();
        double yymax = yAxis.valueToJava2D(ymax, area, getRangeAxisEdge());

        RectShape[] r = new RectShape[] { null, null, null, null };
        if (this.quadrantPaint[0] != null) {
            if (x > xmin && y < ymax) {
                if (this.orientation == PlotOrientation.HORIZONTAL) {
                    r[0] = new RectShape(Math.min(yymax, yy), Math
                            .min(xxmin, xx), Math.abs(yy - yymax), Math.abs(xx
                            - xxmin));
                } else { // PlotOrientation.VERTICAL
                    r[0] = new RectShape(Math.min(xxmin, xx), Math
                            .min(yymax, yy), Math.abs(xx - xxmin), Math.abs(yy
                            - yymax));
                }
                somethingToDraw = true;
            }
        }
        if (this.quadrantPaint[1] != null) {
            if (x < xmax && y < ymax) {
                if (this.orientation == PlotOrientation.HORIZONTAL) {
                    r[1] = new RectShape(Math.min(yymax, yy), Math
                            .min(xxmax, xx), Math.abs(yy - yymax), Math.abs(xx
                            - xxmax));
                } else { // PlotOrientation.VERTICAL
                    r[1] = new RectShape(Math.min(xx, xxmax), Math
                            .min(yymax, yy), Math.abs(xx - xxmax), Math.abs(yy
                            - yymax));
                }
                somethingToDraw = true;
            }
        }
        if (this.quadrantPaint[2] != null) {
            if (x > xmin && y > ymin) {
                if (this.orientation == PlotOrientation.HORIZONTAL) {
                    r[2] = new RectShape(Math.min(yymin, yy), Math
                            .min(xxmin, xx), Math.abs(yy - yymin), Math.abs(xx
                            - xxmin));
                } else { // PlotOrientation.VERTICAL
                    r[2] = new RectShape(Math.min(xxmin, xx), Math
                            .min(yymin, yy), Math.abs(xx - xxmin), Math.abs(yy
                            - yymin));
                }
                somethingToDraw = true;
            }
        }
        if (this.quadrantPaint[3] != null) {
            if (x < xmax && y > ymin) {
                if (this.orientation == PlotOrientation.HORIZONTAL) {
                    r[3] = new RectShape(Math.min(yymin, yy), Math
                            .min(xxmax, xx), Math.abs(yy - yymin), Math.abs(xx
                            - xxmax));
                } else { // PlotOrientation.VERTICAL
                    r[3] = new RectShape(Math.min(xx, xxmax), Math
                            .min(yymin, yy), Math.abs(xx - xxmax), Math.abs(yy
                            - yymin));
                }
                somethingToDraw = true;
            }
        }
        if (somethingToDraw) {

            for (int i = 0; i < 4; i++) {
                if (this.quadrantPaint[i] != null && r[i] != null) {
                    Paint paint = PaintUtility.createPaint(this.quadrantPaint[i]);
                    int oldAlpha = paint.getAlpha();
                    paint.setAlpha(getBackgroundAlpha());
                    r[i].draw(canvas, paint);
                    paint.setAlpha(oldAlpha);
                }
            }

        }
    }

    /**
     * Draws the domain tick bands, if any.
     * 
     * @param canvas
     *            the graphics device.
     * @param dataArea
     *            the data area.
     * @param ticks
     *            the ticks.
     * 
     * @see #setDomainTickBandPaint(PaintType paintType)
     */
    public void drawDomainTickBands(Canvas canvas, RectShape dataArea, List ticks) {
        PaintType bandPaint = getDomainTickBandPaintType();
        if (bandPaint != null) {
            boolean fillBand = false;
            ValueAxis xAxis = getDomainAxis();
            double previous = xAxis.getLowerBound();
            Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                ValueTick tick = (ValueTick) iterator.next();
                double current = tick.getValue();
                if (fillBand) {
                    getRenderer().fillDomainGridBand(canvas, this, xAxis, dataArea,
                            previous, current);
                }
                previous = current;
                fillBand = !fillBand;
            }
            double end = xAxis.getUpperBound();
            if (fillBand) {
                getRenderer().fillDomainGridBand(canvas, this, xAxis, dataArea,
                        previous, end);
            }
        }
    }

    /**
     * Draws the range tick bands, if any.
     * 
     * @param canvas
     *            the graphics device.
     * @param dataArea
     *            the data area.
     * @param ticks
     *            the ticks.
     * 
     * @see #setRangeTickBandPaintType(PaintType paintType)
     */
    public void drawRangeTickBands(Canvas canvas, RectShape dataArea, List ticks) {
        PaintType bandPaint = getRangeTickBandPaintType();
        if (bandPaint != null) {
            boolean fillBand = false;
            ValueAxis axis = getRangeAxis();
            double previous = axis.getLowerBound();
            Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                ValueTick tick = (ValueTick) iterator.next();
                double current = tick.getValue();
                if (fillBand) {
                    getRenderer().fillRangeGridBand(canvas, this, axis, dataArea,
                            previous, current);
                }
                previous = current;
                fillBand = !fillBand;
            }
            double end = axis.getUpperBound();
            if (fillBand) {
                getRenderer().fillRangeGridBand(canvas, this, axis, dataArea,
                        previous, end);
            }
        }
    }

    /**
     * A utility method for drawing the axes.
     * 
     * @param canvas
     *            the graphics device (<code>null</code> not permitted).
     * @param plotArea
     *            the plot area (<code>null</code> not permitted).
     * @param dataArea
     *            the data area (<code>null</code> not permitted).
     * @param plotState
     *            collects information about the plot (<code>null</code>
     *            permitted).
     * 
     * @return A map containing the state for each axis drawn.
     */
    protected Map drawAxes(Canvas canvas, RectShape plotArea,
            RectShape dataArea, PlotRenderingInfo plotState) {

        AxisCollection axisCollection = new AxisCollection();

        // add domain axes to lists...
        for (int index = 0; index < this.domainAxes.size(); index++) {
            ValueAxis axis = (ValueAxis) this.domainAxes.get(index);
            if (axis != null) {
                axisCollection.add(axis, getDomainAxisEdge(index));
            }
        }

        // add range axes to lists...
        for (int index = 0; index < this.rangeAxes.size(); index++) {
            ValueAxis yAxis = (ValueAxis) this.rangeAxes.get(index);
            if (yAxis != null) {
                axisCollection.add(yAxis, getRangeAxisEdge(index));
            }
        }

        Map axisStateMap = new HashMap();

        // draw the top axes
        double cursor = dataArea.getMinY()
                - this.axisOffset.calculateTopOutset(dataArea.getHeight());
        Iterator iterator = axisCollection.getAxesAtTop().iterator();
        while (iterator.hasNext()) {
            ValueAxis axis = (ValueAxis) iterator.next();
            AxisState info = axis.draw(canvas, cursor, plotArea, dataArea,
                    RectangleEdge.TOP, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        }

        // draw the bottom axes
        cursor = dataArea.getMaxY()
                + this.axisOffset.calculateBottomOutset(dataArea.getHeight());
        iterator = axisCollection.getAxesAtBottom().iterator();
        while (iterator.hasNext()) {
            ValueAxis axis = (ValueAxis) iterator.next();
            AxisState info = axis.draw(canvas, cursor, plotArea, dataArea,
                    RectangleEdge.BOTTOM, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        }

        // draw the left axes
        cursor = dataArea.getMinX()
                - this.axisOffset.calculateLeftOutset(dataArea.getWidth());
        iterator = axisCollection.getAxesAtLeft().iterator();
        while (iterator.hasNext()) {
            ValueAxis axis = (ValueAxis) iterator.next();
            AxisState info = axis.draw(canvas, cursor, plotArea, dataArea,
                    RectangleEdge.LEFT, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        }

        // draw the right axes
        cursor = dataArea.getMaxX()
                + this.axisOffset.calculateRightOutset(dataArea.getWidth());
        iterator = axisCollection.getAxesAtRight().iterator();
        while (iterator.hasNext()) {
            ValueAxis axis = (ValueAxis) iterator.next();
            AxisState info = axis.draw(canvas, cursor, plotArea, dataArea,
                    RectangleEdge.RIGHT, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        }

        return axisStateMap;
    }

    /**
     * Draws a representation of the data within the dataArea region, using the
     * current renderer.
     * <P>
     * The <code>info</code> and <code>crosshairState</code> arguments may be
     * <code>null</code>.
     * 
     * @param canvas
     *            the graphics device.
     * @param dataArea
     *            the region in which the data is to be drawn.
     * @param index
     *            the dataset index.
     * @param info
     *            an optional object for collection dimension information.
     * @param crosshairState
     *            collects crosshair information (<code>null</code> permitted).
     * 
     * @return A flag that indicates whether any data was actually rendered.
     */
    public boolean render(Canvas canvas, RectShape dataArea, int index,
            PlotRenderingInfo info, CrosshairState crosshairState) {

        boolean foundData = false;
        XYDataset dataset = getDataset(index);
        if (!DatasetUtilities.isEmptyOrNull(dataset)) {
            foundData = true;
            ValueAxis xAxis = getDomainAxisForDataset(index);
            ValueAxis yAxis = getRangeAxisForDataset(index);
            if (xAxis == null || yAxis == null) {
                return foundData; // can't render anything without axes
            }
            XYItemRenderer renderer = getRenderer(index);
            if (renderer == null) {
                renderer = getRenderer();
                if (renderer == null) { // no default renderer available
                    return foundData;
                }
            }

            XYItemRendererState state = renderer.initialise(canvas, dataArea, this,
                    dataset, info);
            int passCount = renderer.getPassCount();

            SeriesRenderingOrder seriesOrder = getSeriesRenderingOrder();
            if (seriesOrder == SeriesRenderingOrder.REVERSE) {
                // render series in reverse order
                for (int pass = 0; pass < passCount; pass++) {
                    int seriesCount = dataset.getSeriesCount();
                    for (int series = seriesCount - 1; series >= 0; series--) {
                        int firstItem = 0;
                        int lastItem = dataset.getItemCount(series) - 1;
                        if (lastItem == -1) {
                            continue;
                        }
                        if (state.getProcessVisibleItemsOnly()) {
                            int[] itemBounds = RendererUtilities.findLiveItems(
                                    dataset, series, xAxis.getLowerBound(),
                                    xAxis.getUpperBound());
                            firstItem = Math.max(itemBounds[0] - 1, 0);
                            lastItem = Math.min(itemBounds[1] + 1, lastItem);
                        }
                        state.startSeriesPass(dataset, series, firstItem,
                                lastItem, pass, passCount);
                        for (int item = firstItem; item <= lastItem; item++) {
                            renderer.drawItem(canvas, state, dataArea, info, this,
                                    xAxis, yAxis, dataset, series, item,
                                    crosshairState, pass);
                        }
                        state.endSeriesPass(dataset, series, firstItem,
                                lastItem, pass, passCount);
                    }
                }
            } else {
                // render series in forward order
                for (int pass = 0; pass < passCount; pass++) {
                    int seriesCount = dataset.getSeriesCount();
                    for (int series = 0; series < seriesCount; series++) {
                        int firstItem = 0;
                        int lastItem = dataset.getItemCount(series) - 1;
                        if (state.getProcessVisibleItemsOnly()) {
                            int[] itemBounds = RendererUtilities.findLiveItems(
                                    dataset, series, xAxis.getLowerBound(),
                                    xAxis.getUpperBound());
                            firstItem = Math.max(itemBounds[0] - 1, 0);
                            lastItem = Math.min(itemBounds[1] + 1, lastItem);
                        }
                        state.startSeriesPass(dataset, series, firstItem,
                                lastItem, pass, passCount);
                        for (int item = firstItem; item <= lastItem; item++) {
                            renderer.drawItem(canvas, state, dataArea, info, this,
                                    xAxis, yAxis, dataset, series, item,
                                    crosshairState, pass);
                        }
                        state.endSeriesPass(dataset, series, firstItem,
                                lastItem, pass, passCount);
                    }
                }
            }
        }
        return foundData;
    }

    /**
     * Returns the domain axis for a dataset.
     * 
     * @param index
     *            the dataset index.
     * 
     * @return The axis.
     */
    public ValueAxis getDomainAxisForDataset(int index) {
        int upper = Math.max(getDatasetCount(), getRendererCount());
        if (index < 0 || index >= upper) {
            throw new IllegalArgumentException("Index " + index
                    + " out of bounds.");
        }
        ValueAxis valueAxis = null;
        List axisIndices = (List) this.datasetToDomainAxesMap.get(new Integer(
                index));
        if (axisIndices != null) {
            // the first axis in the list is used for data <--> Java2D
            Integer axisIndex = (Integer) axisIndices.get(0);
            valueAxis = getDomainAxis(axisIndex.intValue());
        } else {
            valueAxis = getDomainAxis(0);
        }
        return valueAxis;
    }

    /**
     * Returns the range axis for a dataset.
     * 
     * @param index
     *            the dataset index.
     * 
     * @return The axis.
     */
    public ValueAxis getRangeAxisForDataset(int index) {
        int upper = Math.max(getDatasetCount(), getRendererCount());
        if (index < 0 || index >= upper) {
            throw new IllegalArgumentException("Index " + index
                    + " out of bounds.");
        }
        ValueAxis valueAxis = null;
        List axisIndices = (List) this.datasetToRangeAxesMap.get(new Integer(
                index));
        if (axisIndices != null) {
            // the first axis in the list is used for data <--> Java2D
            Integer axisIndex = (Integer) axisIndices.get(0);
            valueAxis = getRangeAxis(axisIndex.intValue());
        } else {
            valueAxis = getRangeAxis(0);
        }
        return valueAxis;
    }

    /**
     * Draws the gridlines for the plot, if they are visible.
     * 
     * @param canvas
     *            the graphics device.
     * @param dataArea
     *            the data area.
     * @param ticks
     *            the ticks.
     * 
     * @see #drawRangeGridlines(Canvas, RectShape, List)
     */
    protected void drawDomainGridlines(Canvas canvas, RectShape dataArea,
            List ticks) {

        // no renderer, no gridlines...
        if (getRenderer() == null) {
            return;
        }

        // draw the domain grid lines, if any...
        if (isDomainGridlinesVisible() || isDomainMinorGridlinesVisible()) {
            Float gridStroke = null;
            PaintType gridPaintType = null;
            PathEffect gridEffect = null;
            Iterator iterator = ticks.iterator();
            boolean paintLine = false;
            while (iterator.hasNext()) {
                paintLine = false;
                ValueTick tick = (ValueTick) iterator.next();
                if ((tick.getTickType() == TickType.MINOR)
                        && isDomainMinorGridlinesVisible()) {
                    gridStroke = getDomainMinorGridlineStroke();
                    gridPaintType = getDomainMinorGridlinePaintType();
                    gridEffect = getDomainMinorGridlineEffect();
                    paintLine = true;
                } else if ((tick.getTickType() == TickType.MAJOR)
                        && isDomainGridlinesVisible()) {
                    gridStroke = getDomainGridlineStroke();
                    gridPaintType = getDomainGridlinePaintType();
                    gridEffect = getDomainGridlineEffect();
                    paintLine = true;
                }
                XYItemRenderer r = getRenderer();
                if ((r instanceof AbstractXYItemRenderer) && paintLine) {
                    ((AbstractXYItemRenderer) r).drawDomainLine(canvas, this,
                            getDomainAxis(), dataArea, tick.getValue(),
                            gridPaintType, gridStroke, gridEffect);
                }
            }
        }
    }

    /**
     * Draws the gridlines for the plot's primary range axis, if they are
     * visible.
     * 
     * @param canvas
     *            the graphics device.
     * @param area
     *            the data area.
     * @param ticks
     *            the ticks.
     * 
     * @see #drawDomainGridlines(Canvas, RectShape, List)
     */
    protected void drawRangeGridlines(Canvas canvas, RectShape area, List ticks) {

        // no renderer, no gridlines...
        if (getRenderer() == null) {
            return;
        }

        // draw the range grid lines, if any...
        if (isRangeGridlinesVisible() || isRangeMinorGridlinesVisible()) {
            Float gridStroke = null;
            PaintType gridPaint = null;
            PathEffect gridEffect = null;
            ValueAxis axis = getRangeAxis();
            if (axis != null) {
                Iterator iterator = ticks.iterator();
                boolean paintLine = false;
                while (iterator.hasNext()) {
                    paintLine = false;
                    ValueTick tick = (ValueTick) iterator.next();
                    if ((tick.getTickType() == TickType.MINOR)
                            && isRangeMinorGridlinesVisible()) {
                        gridStroke = getRangeMinorGridlineStroke();
                        gridEffect = getRangeMinorGridlineEffect();
                        gridPaint = getRangeMinorGridlinePaintType();
                        paintLine = true;
                    } else if ((tick.getTickType() == TickType.MAJOR)
                            && isRangeGridlinesVisible()) {
                        gridStroke = getRangeGridlineStroke();
                        gridPaint = getRangeGridlinePaintType();
                        gridEffect = getRangeGridlineEffect();
                        paintLine = true;
                    }
                    if ((tick.getValue() != 0.0 || !isRangeZeroBaselineVisible())
                            && paintLine) {
                        getRenderer().drawRangeLine(canvas, this, getRangeAxis(),
                                area, tick.getValue(), gridPaint, gridStroke, gridEffect);
                    }
                }
            }
        }
    }

    /**
     * Draws a base line across the chart at value zero on the domain axis.
     * 
     * @param canvas
     *            the graphics device.
     * @param area
     *            the data area.
     * 
     * @see #setDomainZeroBaselineVisible(boolean)
     * 
     * @since JFreeChart 1.0.5
     */
    protected void drawZeroDomainBaseline(Canvas canvas, RectShape area) {
        if (isDomainZeroBaselineVisible()) {
            XYItemRenderer r = getRenderer();
            // FIXME: the renderer interface doesn't have the drawDomainLine()
            // method, so we have to rely on the renderer being a subclass of
            // AbstractXYItemRenderer (which is lame)
            if (r instanceof AbstractXYItemRenderer) {
                AbstractXYItemRenderer renderer = (AbstractXYItemRenderer) r;
                renderer.drawDomainLine(canvas, this, getDomainAxis(), area, 0.0,
                        this.domainZeroBaselinePaint,
                        this.domainZeroBaselineStroke, 
                        this.domainZeroBaselineEffect);
            }
        }
    }

    /**
     * Draws a base line across the chart at value zero on the range axis.
     * 
     * @param canvas
     *            the graphics device.
     * @param area
     *            the data area.
     * 
     * @see #setRangeZeroBaselineVisible(boolean)
     */
    protected void drawZeroRangeBaseline(Canvas canvas, RectShape area) {
        if (isRangeZeroBaselineVisible()) {
            getRenderer().drawRangeLine(canvas, this, getRangeAxis(), area, 0.0,
                    this.rangeZeroBaselinePaint, 
                    this.rangeZeroBaselineStroke, 
                    this.rangeZeroBaselineEffect);
        }
    }

    /**
     * Draws the annotations for the plot.
     * 
     * @param canvas
     *            the graphics device.
     * @param dataArea
     *            the data area.
     * @param info
     *            the chart rendering info.
     */
    public void drawAnnotations(Canvas canvas, RectShape dataArea,
            PlotRenderingInfo info) {

        Iterator iterator = this.annotations.iterator();
        while (iterator.hasNext()) {
            XYAnnotation annotation = (XYAnnotation) iterator.next();
            ValueAxis xAxis = getDomainAxis();
            ValueAxis yAxis = getRangeAxis();
            annotation.draw(canvas, this, dataArea, xAxis, yAxis, 0, info);
        }

    }

    /**
     * Draws the domain markers (if any) for an axis and layer. This method is
     * typically called from within the draw() method.
     * 
     * @param canvas
     *            the graphics device.
     * @param dataArea
     *            the data area.
     * @param index
     *            the renderer index.
     * @param layer
     *            the layer (foreground or background).
     */
    protected void drawDomainMarkers(Canvas canvas, RectShape dataArea,
            int index, Layer layer) {

        XYItemRenderer r = getRenderer(index);
        if (r == null) {
            return;
        }
        // check that the renderer has a corresponding dataset (it doesn't
        // matter if the dataset is null)
        if (index >= getDatasetCount()) {
            return;
        }
        Collection markers = getDomainMarkers(index, layer);
        ValueAxis axis = getDomainAxisForDataset(index);
        if (markers != null && axis != null) {
            Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                Marker marker = (Marker) iterator.next();
                r.drawDomainMarker(canvas, this, axis, marker, dataArea);
            }
        }

    }

    /**
     * Draws the range markers (if any) for a renderer and layer. This method is
     * typically called from within the draw() method.
     * 
     * @param canvas
     *            the graphics device.
     * @param dataArea
     *            the data area.
     * @param index
     *            the renderer index.
     * @param layer
     *            the layer (foreground or background).
     */
    protected void drawRangeMarkers(Canvas canvas, RectShape dataArea, int index,
            Layer layer) {

        XYItemRenderer r = getRenderer(index);
        if (r == null) {
            return;
        }
        // check that the renderer has a corresponding dataset (it doesn't
        // matter if the dataset is null)
        if (index >= getDatasetCount()) {
            return;
        }
        Collection markers = getRangeMarkers(index, layer);
        ValueAxis axis = getRangeAxisForDataset(index);
        if (markers != null && axis != null) {
            Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                Marker marker = (Marker) iterator.next();
                r.drawRangeMarker(canvas, this, axis, marker, dataArea);
            }
        }
    }

    /**
     * Returns the list of domain markers (read only) for the specified layer.
     * 
     * @param layer
     *            the layer (foreground or background).
     * 
     * @return The list of domain markers.
     * 
     * @see #getRangeMarkers(Layer)
     */
    public Collection getDomainMarkers(Layer layer) {
        return getDomainMarkers(0, layer);
    }

    /**
     * Returns the list of range markers (read only) for the specified layer.
     * 
     * @param layer
     *            the layer (foreground or background).
     * 
     * @return The list of range markers.
     * 
     * @see #getDomainMarkers(Layer)
     */
    public Collection getRangeMarkers(Layer layer) {
        return getRangeMarkers(0, layer);
    }

    /**
     * Returns a collection of domain markers for a particular renderer and
     * layer.
     * 
     * @param index
     *            the renderer index.
     * @param layer
     *            the layer.
     * 
     * @return A collection of markers (possibly <code>null</code>).
     * 
     * @see #getRangeMarkers(int, Layer)
     */
    public Collection getDomainMarkers(int index, Layer layer) {
        Collection result = null;
        Integer key = new Integer(index);
        if (layer == Layer.FOREGROUND) {
            result = (Collection) this.foregroundDomainMarkers.get(key);
        } else if (layer == Layer.BACKGROUND) {
            result = (Collection) this.backgroundDomainMarkers.get(key);
        }
        if (result != null) {
            result = Collections.unmodifiableCollection(result);
        }
        return result;
    }

    /**
     * Returns a collection of range markers for a particular renderer and
     * layer.
     * 
     * @param index
     *            the renderer index.
     * @param layer
     *            the layer.
     * 
     * @return A collection of markers (possibly <code>null</code>).
     * 
     * @see #getDomainMarkers(int, Layer)
     */
    public Collection getRangeMarkers(int index, Layer layer) {
        Collection result = null;
        Integer key = new Integer(index);
        if (layer == Layer.FOREGROUND) {
            result = (Collection) this.foregroundRangeMarkers.get(key);
        } else if (layer == Layer.BACKGROUND) {
            result = (Collection) this.backgroundRangeMarkers.get(key);
        }
        if (result != null) {
            result = Collections.unmodifiableCollection(result);
        }
        return result;
    }

    /**
     * Utility method for drawing a horizontal line across the data area of the
     * plot.
     * 
     * @param canvas
     *            the graphics device.
     * @param dataArea
     *            the data area.
     * @param value
     *            the coordinate, where to draw the line.
     * @param stroke
     *            the stroke to use.
     * @param paint
     *            the paint to use.
     */
    protected void drawHorizontalLine(Canvas canvas, RectShape dataArea,
            double value, Float stroke, Paint paint) {

        ValueAxis axis = getRangeAxis();
        if (getOrientation() == PlotOrientation.HORIZONTAL) {
            axis = getDomainAxis();
        }
        if (axis.getRange().contains(value)) {
            double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
            LineShape line = new LineShape(dataArea.getMinX(), yy, dataArea
                    .getMaxX(), yy);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke);
            canvas.drawLine((float) line.getX1(), (float) line.getY1(),
                    (float) line.getX2(), (float) line.getY2(), paint);
        }

    }

    /**
     * Draws a domain crosshair.
     * 
     * @param canvas
     *            the graphics target.
     * @param dataArea
     *            the data area.
     * @param orientation
     *            the plot orientation.
     * @param value
     *            the crosshair value.
     * @param axis
     *            the axis against which the value is measured.
     * @param stroke
     *            the stroke used to draw the crosshair line.
     * @param paintType
     *            the paintType used to draw the crosshair line.
     * @param pathEffect
     *            the pathEffect used to draw the crosshair line.
     * 
     * @since JFreeChart 1.0.4
     */
    protected void drawDomainCrosshair(Canvas canvas, RectShape dataArea,
            PlotOrientation orientation, double value, ValueAxis axis,
            Float stroke, PaintType paintType, PathEffect pathEffect) {

        if (axis.getRange().contains(value)) {
            LineShape line = null;
            if (orientation == PlotOrientation.VERTICAL) {
                double xx = axis.valueToJava2D(value, dataArea,
                        RectangleEdge.BOTTOM);
                line = new LineShape(xx, dataArea.getMinY(), xx, dataArea
                        .getMaxY());
            } else {
                double yy = axis.valueToJava2D(value, dataArea,
                        RectangleEdge.LEFT);
                line = new LineShape(dataArea.getMinX(), yy, dataArea
                        .getMaxX(), yy);
            }
            
            Paint paint = PaintUtility.createPaint(paintType, stroke, pathEffect);

            line.draw(canvas, paint);
        }

    }

    /**
     * Utility method for drawing a vertical line on the data area of the plot.
     * 
     * @param canvas
     *            the graphics device.
     * @param dataArea
     *            the data area.
     * @param value
     *            the coordinate, where to draw the line.
     * @param stroke
     *            the stroke to use.
     * @param paint
     *            the paint to use.
     */
    protected void drawVerticalLine(Canvas canvas, RectShape dataArea,
            double value, Float stroke, Paint paint) {

        ValueAxis axis = getDomainAxis();
        if (getOrientation() == PlotOrientation.HORIZONTAL) {
            axis = getRangeAxis();
        }
        if (axis.getRange().contains(value)) {
            double xx = axis.valueToJava2D(value, dataArea,
                    RectangleEdge.BOTTOM);
            LineShape line = new LineShape(xx, dataArea.getMinY(), xx,
                    dataArea.getMaxY());
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke);
            canvas.drawLine((float) line.getX1(), (float) line.getY1(),
                    (float) line.getX2(), (float) line.getY2(), paint);
        }

    }

    /**
     * Draws a range crosshair.
     * 
     * @param canvas
     *            the graphics target.
     * @param dataArea
     *            the data area.
     * @param orientation
     *            the plot orientation.
     * @param value
     *            the crosshair value.
     * @param axis
     *            the axis against which the value is measured.
     * @param stroke
     *            the stroke used to draw the crosshair line.
     * @param paintType
     *            the paint used to draw the crosshair line.
     * @param effect
     *            the effect used to draw the crosshair line.
     * 
     * @since JFreeChart 1.0.4
     */
    protected void drawRangeCrosshair(Canvas canvas, RectShape dataArea,
            PlotOrientation orientation, double value, ValueAxis axis,
            Float stroke, PaintType paintType, PathEffect effect) {

        if (axis.getRange().contains(value)) {
            LineShape line = null;
            if (orientation == PlotOrientation.HORIZONTAL) {
                double xx = axis.valueToJava2D(value, dataArea,
                        RectangleEdge.BOTTOM);
                line = new LineShape(xx, dataArea.getMinY(), xx, dataArea
                        .getMaxY());
            } else {
                double yy = axis.valueToJava2D(value, dataArea,
                        RectangleEdge.LEFT);
                line = new LineShape(dataArea.getMinX(), yy, dataArea
                        .getMaxX(), yy);
            }
            
            Paint paint = PaintUtility.createPaint(Paint.ANTI_ALIAS_FLAG, paintType, stroke, effect);
            paint.setAlpha(paintType.getAlpha());
            line.draw(canvas, paint);
        }

    }

    /**
     * Handles a 'click' on the plot by updating the anchor values.
     * 
     * @param x
     *            the x-coordinate, where the click occurred, in Java2D space.
     * @param y
     *            the y-coordinate, where the click occurred, in Java2D space.
     * @param info
     *            object containing information about the plot dimensions.
     */
    public void handleClick(int x, int y, PlotRenderingInfo info) {

        RectShape dataArea = info.getDataArea();
        if (dataArea.contains(x, y)) {
            // set the anchor value for the horizontal axis...
            ValueAxis xaxis = getDomainAxis();
            if (xaxis != null) {
                double hvalue = xaxis.java2DToValue(x, info.getDataArea(),
                        getDomainAxisEdge());
                setDomainCrosshairValue(hvalue);
            }

            // set the anchor value for the vertical axis...
            ValueAxis yaxis = getRangeAxis();
            if (yaxis != null) {
                double vvalue = yaxis.java2DToValue(y, info.getDataArea(),
                        getRangeAxisEdge());
                setRangeCrosshairValue(vvalue);
            }
        }
    }

    /**
     * A utility method that returns a list of datasets that are mapped to a
     * particular axis.
     * 
     * @param axisIndex
     *            the axis index (<code>null</code> not permitted).
     * 
     * @return A list of datasets.
     */
    private List getDatasetsMappedToDomainAxis(Integer axisIndex) {
        if (axisIndex == null) {
            throw new IllegalArgumentException("Null 'axisIndex' argument.");
        }
        List result = new ArrayList();
        for (int i = 0; i < this.datasets.size(); i++) {
            List mappedAxes = (List) this.datasetToDomainAxesMap
                    .get(new Integer(i));
            if (mappedAxes == null) {
                if (axisIndex.equals(ZERO)) {
                    result.add(this.datasets.get(i));
                }
            } else {
                if (mappedAxes.contains(axisIndex)) {
                    result.add(this.datasets.get(i));
                }
            }
        }
        return result;
    }

    /**
     * A utility method that returns a list of datasets that are mapped to a
     * particular axis.
     * 
     * @param axisIndex
     *            the axis index (<code>null</code> not permitted).
     * 
     * @return A list of datasets.
     */
    private List getDatasetsMappedToRangeAxis(Integer axisIndex) {
        if (axisIndex == null) {
            throw new IllegalArgumentException("Null 'axisIndex' argument.");
        }
        List result = new ArrayList();
        for (int i = 0; i < this.datasets.size(); i++) {
            List mappedAxes = (List) this.datasetToRangeAxesMap
                    .get(new Integer(i));
            if (mappedAxes == null) {
                if (axisIndex.equals(ZERO)) {
                    result.add(this.datasets.get(i));
                }
            } else {
                if (mappedAxes.contains(axisIndex)) {
                    result.add(this.datasets.get(i));
                }
            }
        }
        return result;
    }

    /**
     * Returns the index of the given domain axis.
     * 
     * @param axis
     *            the axis.
     * 
     * @return The axis index.
     * 
     * @see #getRangeAxisIndex(ValueAxis)
     */
    public int getDomainAxisIndex(ValueAxis axis) {
        int result = this.domainAxes.indexOf(axis);
        if (result < 0) {
            // try the parent plot
            Plot parent = getParent();
            if (parent instanceof XYPlot) {
                XYPlot p = (XYPlot) parent;
                result = p.getDomainAxisIndex(axis);
            }
        }
        return result;
    }

    /**
     * Returns the index of the given range axis.
     * 
     * @param axis
     *            the axis.
     * 
     * @return The axis index.
     * 
     * @see #getDomainAxisIndex(ValueAxis)
     */
    public int getRangeAxisIndex(ValueAxis axis) {
        int result = this.rangeAxes.indexOf(axis);
        if (result < 0) {
            // try the parent plot
            Plot parent = getParent();
            if (parent instanceof XYPlot) {
                XYPlot p = (XYPlot) parent;
                result = p.getRangeAxisIndex(axis);
            }
        }
        return result;
    }

    /**
     * Returns the range for the specified axis.
     * 
     * @param axis
     *            the axis.
     * 
     * @return The range.
     */
    public Range getDataRange(ValueAxis axis) {

        Range result = null;
        List mappedDatasets = new ArrayList();
        List includedAnnotations = new ArrayList();
        boolean isDomainAxis = true;

        // is it a domain axis?
        int domainIndex = getDomainAxisIndex(axis);
        if (domainIndex >= 0) {
            isDomainAxis = true;
            mappedDatasets.addAll(getDatasetsMappedToDomainAxis(new Integer(
                    domainIndex)));
            if (domainIndex == 0) {
                // grab the plot's annotations
                Iterator iterator = this.annotations.iterator();
                while (iterator.hasNext()) {
                    XYAnnotation annotation = (XYAnnotation) iterator.next();
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
                }
            }
        }

        // or is it a range axis?
        int rangeIndex = getRangeAxisIndex(axis);
        if (rangeIndex >= 0) {
            isDomainAxis = false;
            mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new Integer(
                    rangeIndex)));
            if (rangeIndex == 0) {
                Iterator iterator = this.annotations.iterator();
                while (iterator.hasNext()) {
                    XYAnnotation annotation = (XYAnnotation) iterator.next();
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
                }
            }
        }

        // iterate through the datasets that map to the axis and get the union
        // of the ranges.
        Iterator iterator = mappedDatasets.iterator();
        while (iterator.hasNext()) {
            XYDataset d = (XYDataset) iterator.next();
            if (d != null) {
                XYItemRenderer r = getRendererForDataset(d);
                if (isDomainAxis) {
                    if (r != null) {
                        result = Range.combine(result, r.findDomainBounds(d));
                    } else {
                        result = Range.combine(result, DatasetUtilities
                                .findDomainBounds(d));
                    }
                } else {
                    if (r != null) {
                        result = Range.combine(result, r.findRangeBounds(d));
                    } else {
                        result = Range.combine(result, DatasetUtilities
                                .findRangeBounds(d));
                    }
                }
                // FIXME: the XYItemRenderer interface doesn't specify the
                // getAnnotations() method but it should
                if (r instanceof AbstractXYItemRenderer) {
                    AbstractXYItemRenderer rr = (AbstractXYItemRenderer) r;
                    Collection c = rr.getAnnotations();
                    Iterator i = c.iterator();
                    while (i.hasNext()) {
                        XYAnnotation a = (XYAnnotation) i.next();
                        if (a instanceof XYAnnotationBoundsInfo) {
                            includedAnnotations.add(a);
                        }
                    }
                }
            }
        }

        Iterator it = includedAnnotations.iterator();
        while (it.hasNext()) {
            XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) it.next();
            if (xyabi.getIncludeInDataBounds()) {
                if (isDomainAxis) {
                    result = Range.combine(result, xyabi.getXRange());
                } else {
                    result = Range.combine(result, xyabi.getYRange());
                }
            }
        }

        return result;

    }

    /*  *//**
     * Receives notification of a change to the plot's dataset.
     * <P>
     * The axis ranges are updated if necessary.
     * 
     * @param event
     *            information about the event (not used here).
     */
     public void datasetChanged(DatasetChangeEvent event) {
         configureDomainAxes();
         configureRangeAxes();
         if (getParent() != null) {
             getParent().datasetChanged(event);
         } else {
             PlotChangeEvent e = new PlotChangeEvent(this);
             e.setType(ChartChangeEventType.DATASET_UPDATED);
             notifyListeners(e);
         }
     }
     

    /**
     * Receives notification of a renderer change event.
     * 
     * @param event
     *            the event.
     */
    public void rendererChanged(RendererChangeEvent event) {
        // if the event was caused by a change to series visibility, then
        // the axis ranges might need updating...
        if (event.getSeriesVisibilityChanged()) {
            configureDomainAxes();
            configureRangeAxes();
        }
        fireChangeEvent();
    }

    /**
     * Returns a flag indicating whether or not the domain crosshair is visible.
     * 
     * @return The flag.
     * 
     * @see #setDomainCrosshairVisible(boolean)
     */
    public boolean isDomainCrosshairVisible() {
        return this.domainCrosshairVisible;
    }

    /**
     * Sets the flag indicating whether or not the domain crosshair is visible
     * and, if the flag changes, sends a {@link PlotChangeEvent} to all
     * registered listeners.
     * 
     * @param flag
     *            the new value of the flag.
     * 
     * @see #isDomainCrosshairVisible()
     */
    public void setDomainCrosshairVisible(boolean flag) {
        if (this.domainCrosshairVisible != flag) {
            this.domainCrosshairVisible = flag;
            fireChangeEvent();
        }
    }

    /**
     * Returns a flag indicating whether or not the crosshair should "lock-on"
     * to actual data values.
     * 
     * @return The flag.
     * 
     * @see #setDomainCrosshairLockedOnData(boolean)
     */
    public boolean isDomainCrosshairLockedOnData() {
        return this.domainCrosshairLockedOnData;
    }

    /**
     * Sets the flag indicating whether or not the domain crosshair should
     * "lock-on" to actual data values. If the flag value changes, this method
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param flag
     *            the flag.
     * 
     * @see #isDomainCrosshairLockedOnData()
     */
    public void setDomainCrosshairLockedOnData(boolean flag) {
        if (this.domainCrosshairLockedOnData != flag) {
            this.domainCrosshairLockedOnData = flag;
            fireChangeEvent();
        }
    }

    /**
     * Returns the domain crosshair value.
     * 
     * @return The value.
     * 
     * @see #setDomainCrosshairValue(double)
     */
    public double getDomainCrosshairValue() {
        return this.domainCrosshairValue;
    }

    /**
     * Sets the domain crosshair value and sends a {@link PlotChangeEvent} to
     * all registered listeners (provided that the domain crosshair is visible).
     * 
     * @param value
     *            the value.
     * 
     * @see #getDomainCrosshairValue()
     */
    public void setDomainCrosshairValue(double value) {
        setDomainCrosshairValue(value, true);
    }

    /**
     * Sets the domain crosshair value and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners (provided that the
     * domain crosshair is visible).
     * 
     * @param value
     *            the new value.
     * @param notify
     *            notify listeners?
     * 
     * @see #getDomainCrosshairValue()
     */
    public void setDomainCrosshairValue(double value, boolean notify) {
        this.domainCrosshairValue = value;
        if (isDomainCrosshairVisible() && notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the {@link Stroke} used to draw the crosshair (if visible).
     * 
     * @return The crosshair stroke (never <code>null</code>).
     * 
     * @see #setDomainCrosshairStroke(Float stroke)
     * @see #isDomainCrosshairVisible()
     * @see #getDomainCrosshairPaintType()
     * @see #getDomainCrosshairEffect()
     */
    public Float getDomainCrosshairStroke() {
        return this.domainCrosshairStroke;
    }

    /**
     * Sets the Stroke used to draw the crosshairs (if visible) and notifies
     * registered listeners that the axis has been modified.
     * 
     * @param stroke
     *            the new crosshair stroke (<code>null</code> not permitted).
     * 
     * @see #getDomainCrosshairStroke()
     */
    public void setDomainCrosshairStroke(Float stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.domainCrosshairStroke = stroke;
        fireChangeEvent();
    }

    /**
     * Returns the domain crosshair paint.
     * 
     * @return The crosshair paint (never <code>null</code>).
     * 
     * @see #setDomainCrosshairPaintType(PaintType paintType)
     * @see #isDomainCrosshairVisible()
     * @see #getDomainCrosshairStroke()
     * @see #getDomainCrosshairEffect()
     */
    public PaintType getDomainCrosshairPaintType() {
        return this.domainCrosshairPaintType;
    }

    /**
     * Sets the paint used to draw the crosshairs (if visible) and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param paintType
     *            the new crosshair paint (<code>null</code> not permitted).
     * 
     * @see #getDomainCrosshairPaintType()
     */
    public void setDomainCrosshairPaintType(PaintType paintType) {
        if (paintType == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainCrosshairPaintType = paintType;
        fireChangeEvent();
    }

    /**
     * Returns the {@link PathEffect} used to draw the crosshair (if visible).
     * 
     * @return The crosshair effect (never <code>null</code>).
     * 
     * @see #setDomainCrosshairEffect(PathEffect effect)
     * @see #isDomainCrosshairVisible()
     * @see #getDomainCrosshairPaintType()
     * @see #getDomainCrosshairStroke()
     */
    public PathEffect getDomainCrosshairEffect() {
        return this.domainCrosshairEffect;
    }

    /**
     * Sets the effect used to draw the crosshairs (if visible) and notifies
     * registered listeners that the axis has been modified.
     * 
     * @param effect
     *            the new crosshair effect (<code>null</code> not permitted).
     * 
     * @see #getDomainCrosshairEffect()
     */
    public void setDomainCrosshairEffect(PathEffect pathEffect) {
        if (pathEffect == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainCrosshairEffect = pathEffect;
        fireChangeEvent();
    }

    /**
     * Returns a flag indicating whether or not the range crosshair is visible.
     * 
     * @return The flag.
     * 
     * @see #setRangeCrosshairVisible(boolean)
     * @see #isDomainCrosshairVisible()
     */
    public boolean isRangeCrosshairVisible() {
        return this.rangeCrosshairVisible;
    }

    /**
     * Sets the flag indicating whether or not the range crosshair is visible.
     * If the flag value changes, this method sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param flag
     *            the new value of the flag.
     * 
     * @see #isRangeCrosshairVisible()
     */
    public void setRangeCrosshairVisible(boolean flag) {
        if (this.rangeCrosshairVisible != flag) {
            this.rangeCrosshairVisible = flag;
            fireChangeEvent();
        }
    }

    /**
     * Returns a flag indicating whether or not the crosshair should "lock-on"
     * to actual data values.
     * 
     * @return The flag.
     * 
     * @see #setRangeCrosshairLockedOnData(boolean)
     */
    public boolean isRangeCrosshairLockedOnData() {
        return this.rangeCrosshairLockedOnData;
    }

    /**
     * Sets the flag indicating whether or not the range crosshair should
     * "lock-on" to actual data values. If the flag value changes, this method
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param flag
     *            the flag.
     * 
     * @see #isRangeCrosshairLockedOnData()
     */
    public void setRangeCrosshairLockedOnData(boolean flag) {
        if (this.rangeCrosshairLockedOnData != flag) {
            this.rangeCrosshairLockedOnData = flag;
            fireChangeEvent();
        }
    }

    /**
     * Returns the range crosshair value.
     * 
     * @return The value.
     * 
     * @see #setRangeCrosshairValue(double)
     */
    public double getRangeCrosshairValue() {
        return this.rangeCrosshairValue;
    }

    /**
     * Sets the range crosshair value.
     * <P>
     * Registered listeners are notified that the plot has been modified, but
     * only if the crosshair is visible.
     * 
     * @param value
     *            the new value.
     * 
     * @see #getRangeCrosshairValue()
     */
    public void setRangeCrosshairValue(double value) {
        setRangeCrosshairValue(value, true);
    }

    /**
     * Sets the range crosshair value and sends a {@link PlotChangeEvent} to all
     * registered listeners, but only if the crosshair is visible.
     * 
     * @param value
     *            the new value.
     * @param notify
     *            a flag that controls whether or not listeners are notified.
     * 
     * @see #getRangeCrosshairValue()
     */
    public void setRangeCrosshairValue(double value, boolean notify) {
        this.rangeCrosshairValue = value;
        if (isRangeCrosshairVisible() && notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the stroke used to draw the crosshair (if visible).
     * 
     * @return The crosshair stroke (never <code>null</code>).
     * 
     * @see #setRangeCrosshairStroke(Float stroke)
     * @see #isRangeCrosshairVisible()
     * @see #getRangeCrosshairPaintType()
     * @see #getRangeCrosshairEffect()
     */
    public Float getRangeCrosshairStroke() {
        return this.rangeCrosshairStroke;
    }

    /**
     * Sets the stroke used to draw the crosshairs (if visible) and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param stroke
     *            the new crosshair stroke (<code>null</code> not permitted).
     * 
     * @see #getRangeCrosshairStroke()
     */
    public void setRangeCrosshairStroke(Float stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeCrosshairStroke = stroke;
        fireChangeEvent();
    }
    
    /**
     * Returns the effect used to draw the crosshair (if visible).
     * 
     * @return The crosshair effect (never <code>null</code>).
     * 
     * @see #setRangeCrosshairEffect(PathEffect effect)
     * @see #isRangeCrosshairVisible()
     * @see #getRangeCrosshairPaintType()
     * @see #getRangeCrosshairStroke()
     */
    public PathEffect getRangeCrosshairEffect() {
        return this.rangeCrosshairEffect;
    }
    
    /**
     * Sets the effect used to draw the crosshairs (if visible) and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param effect
     *            the new crosshair effect (<code>null</code> not permitted).
     * 
     * @see #getRangeCrosshairEffect()
     */
    public void setRangeCrosshairEffect(PathEffect effect) {
        this.rangeCrosshairEffect = effect;
        fireChangeEvent();
    }

    /**
     * Returns the range crosshair paint.
     * 
     * @return The crosshair paint (never <code>null</code>).
     * 
     * @see #setRangeCrosshairPaintType(PaintType paintType)
     * @see #isRangeCrosshairVisible()
     * @see #getRangeCrosshairStroke()
     * @see #getRangeCrosshairEffect()
     */
    public PaintType getRangeCrosshairPaintType() {
        return this.rangeCrosshairPaintType;
    }

    /**
     * Sets the paint used to color the crosshairs (if visible) and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param paintType
     *            the new crosshair paint (<code>null</code> not permitted).
     * 
     * @see #getRangeCrosshairPaintType()
     */
    public void setRangeCrosshairPaintType(PaintType paintType) {
        if (paintType == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeCrosshairPaintType = paintType;
        fireChangeEvent();
    }

    /**
     * Returns the fixed domain axis space.
     * 
     * @return The fixed domain axis space (possibly <code>null</code>).
     * 
     * @see #setFixedDomainAxisSpace(AxisSpace)
     */
    public AxisSpace getFixedDomainAxisSpace() {
        return this.fixedDomainAxisSpace;
    }

    /**
     * Sets the fixed domain axis space and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param space
     *            the space (<code>null</code> permitted).
     * 
     * @see #getFixedDomainAxisSpace()
     */
    public void setFixedDomainAxisSpace(AxisSpace space) {
        setFixedDomainAxisSpace(space, true);
    }

    /**
     * Sets the fixed domain axis space and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param space
     *            the space (<code>null</code> permitted).
     * @param notify
     *            notify listeners?
     * 
     * @see #getFixedDomainAxisSpace()
     * 
     * @since JFreeChart 1.0.9
     */
    public void setFixedDomainAxisSpace(AxisSpace space, boolean notify) {
        this.fixedDomainAxisSpace = space;
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the fixed range axis space.
     * 
     * @return The fixed range axis space (possibly <code>null</code>).
     * 
     * @see #setFixedRangeAxisSpace(AxisSpace)
     */
    public AxisSpace getFixedRangeAxisSpace() {
        return this.fixedRangeAxisSpace;
    }

    /**
     * Sets the fixed range axis space and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     * 
     * @param space
     *            the space (<code>null</code> permitted).
     * 
     * @see #getFixedRangeAxisSpace()
     */
    public void setFixedRangeAxisSpace(AxisSpace space) {
        setFixedRangeAxisSpace(space, true);
    }

    /**
     * Sets the fixed range axis space and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param space
     *            the space (<code>null</code> permitted).
     * @param notify
     *            notify listeners?
     * 
     * @see #getFixedRangeAxisSpace()
     * 
     * @since JFreeChart 1.0.9
     */
    public void setFixedRangeAxisSpace(AxisSpace space, boolean notify) {
        this.fixedRangeAxisSpace = space;
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns <code>true</code> if panning is enabled for the domain axes, and
     * <code>false</code> otherwise.
     * 
     * @return A boolean.
     * 
     * @since JFreeChart 1.0.13
     */
    public boolean isDomainPannable() {
        return this.domainPannable;
    }

    /**
     * Sets the flag that enables or disables panning of the plot along the
     * domain axes.
     * 
     * @param pannable
     *            the new flag value.
     * 
     * @since JFreeChart 1.0.13
     */
    public void setDomainPannable(boolean pannable) {
        this.domainPannable = pannable;
    }

    /**
     * Returns <code>true</code> if panning is enabled for the range axes, and
     * <code>false</code> otherwise.
     * 
     * @return A boolean.
     * 
     * @since JFreeChart 1.0.13
     */
    public boolean isRangePannable() {
        return this.rangePannable;
    }

    /**
     * Sets the flag that enables or disables panning of the plot along the
     * range axes.
     * 
     * @param pannable
     *            the new flag value.
     * 
     * @since JFreeChart 1.0.13
     */
    public void setRangePannable(boolean pannable) {
        this.rangePannable = pannable;
    }

    /**
     * Pans the domain axes by the specified percentage.
     * 
     * @param percent
     *            the distance to pan (as a percentage of the axis length).
     * @param info
     *            the plot info
     * @param source
     *            the source point where the pan action started.
     * 
     * @since JFreeChart 1.0.13
     */
    public void panDomainAxes(double percent, PlotRenderingInfo info,
            PointF source) {
        if (!isDomainPannable()) {
            return;
        }
        int domainAxisCount = getDomainAxisCount();
        for (int i = 0; i < domainAxisCount; i++) {
            ValueAxis axis = getDomainAxis(i);
            if (axis == null) {
                continue;
            }
            if (axis.isInverted()) {
                percent = -percent;
            }
            axis.pan(percent);
        }
    }

    /**
     * Pans the range axes by the specified percentage.
     * 
     * @param percent
     *            the distance to pan (as a percentage of the axis length).
     * @param info
     *            the plot info
     * @param source
     *            the source point where the pan action started.
     * 
     * @since JFreeChart 1.0.13
     */
    public void panRangeAxes(double percent, PlotRenderingInfo info,
            PointF source) {
        if (!isRangePannable()) {
            return;
        }
        int rangeAxisCount = getRangeAxisCount();
        for (int i = 0; i < rangeAxisCount; i++) {
            ValueAxis axis = getRangeAxis(i);
            if (axis == null) {
                continue;
            }
            if (axis.isInverted()) {
                percent = -percent;
            }
            axis.pan(percent);
        }
    }

    /**
     * Multiplies the range on the domain axis/axes by the specified factor.
     * 
     * @param factor
     *            the zoom factor.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point (in Java2D space).
     * 
     * @see #zoomRangeAxes(double, PlotRenderingInfo, PointF)
     */
    public void zoomDomainAxes(double factor, PlotRenderingInfo info,
            PointF source) {
        // delegate to other method
        zoomDomainAxes(factor, info, source, false);
    }

    /**
     * Multiplies the range on the domain axis/axes by the specified factor.
     * 
     * @param factor
     *            the zoom factor.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point (in Java2D space).
     * @param useAnchor
     *            use source point as zoom anchor?
     * 
     * @see #zoomRangeAxes(double, PlotRenderingInfo, PointF, boolean)
     * 
     * @since JFreeChart 1.0.7
     */
    public void zoomDomainAxes(double factor, PlotRenderingInfo info,
            PointF source, boolean useAnchor) {

        // perform the zoom on each domain axis
        for (int i = 0; i < this.domainAxes.size(); i++) {
            ValueAxis domainAxis = (ValueAxis) this.domainAxes.get(i);
            if (domainAxis != null) {
                if (useAnchor) {
                    // get the relevant source coordinate given the plot
                    // orientation
                    double sourceX = source.x;
                    if (this.orientation == PlotOrientation.HORIZONTAL) {
                        sourceX = source.y;
                    }
                    double anchorX = domainAxis.java2DToValue(sourceX, info
                            .getDataArea(), getDomainAxisEdge());
                    domainAxis.resizeRange2(factor, anchorX);
                } else {
                    domainAxis.resizeRange(factor);
                }
            }
        }
    }

    /**
     * Zooms in on the domain axis/axes. The new lower and upper bounds are
     * specified as percentages of the current axis range, where 0 percent is
     * the current lower bound and 100 percent is the current upper bound.
     * 
     * @param lowerPercent
     *            a percentage that determines the new lower bound for the axis
     *            (e.g. 0.20 is twenty percent).
     * @param upperPercent
     *            a percentage that determines the new upper bound for the axis
     *            (e.g. 0.80 is eighty percent).
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point (ignored).
     * 
     * @see #zoomRangeAxes(double, double, PlotRenderingInfo, PointF)
     */
    public void zoomDomainAxes(double lowerPercent, double upperPercent,
            PlotRenderingInfo info, PointF source) {
        for (int i = 0; i < this.domainAxes.size(); i++) {
            ValueAxis domainAxis = (ValueAxis) this.domainAxes.get(i);
            if (domainAxis != null) {
                domainAxis.zoomRange(lowerPercent, upperPercent);
            }
        }
    }

    /**
     * Multiplies the range on the range axis/axes by the specified factor.
     * 
     * @param factor
     *            the zoom factor.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point.
     * 
     * @see #zoomDomainAxes(double, PlotRenderingInfo, PointF, boolean)
     */
    public void zoomRangeAxes(double factor, PlotRenderingInfo info,
            PointF source) {
        // delegate to other method
        zoomRangeAxes(factor, info, source, false);
    }

    /**
     * Multiplies the range on the range axis/axes by the specified factor.
     * 
     * @param factor
     *            the zoom factor.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point.
     * @param useAnchor
     *            a flag that controls whether or not the source point is used
     *            for the zoom anchor.
     * 
     * @see #zoomDomainAxes(double, PlotRenderingInfo, PointF, boolean)
     * 
     * @since JFreeChart 1.0.7
     */
    public void zoomRangeAxes(double factor, PlotRenderingInfo info,
            PointF source, boolean useAnchor) {

        // perform the zoom on each range axis
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            ValueAxis rangeAxis = (ValueAxis) this.rangeAxes.get(i);
            if (rangeAxis != null) {
                if (useAnchor) {
                    // get the relevant source coordinate given the plot
                    // orientation
                    double sourceY = source.y;
                    if (this.orientation == PlotOrientation.HORIZONTAL) {
                        sourceY = source.x;
                    }
                    double anchorY = rangeAxis.java2DToValue(sourceY, info
                            .getDataArea(), getRangeAxisEdge());
                    rangeAxis.resizeRange2(factor, anchorY);
                } else {
                    rangeAxis.resizeRange(factor);
                }
            }
        }
    }

    /**
     * Zooms in on the range axes.
     * 
     * @param lowerPercent
     *            the lower bound.
     * @param upperPercent
     *            the upper bound.
     * @param info
     *            the plot rendering info.
     * @param source
     *            the source point.
     * 
     * @see #zoomDomainAxes(double, double, PlotRenderingInfo, PointF)
     */
    public void zoomRangeAxes(double lowerPercent, double upperPercent,
            PlotRenderingInfo info, PointF source) {
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            ValueAxis rangeAxis = (ValueAxis) this.rangeAxes.get(i);
            if (rangeAxis != null) {
                rangeAxis.zoomRange(lowerPercent, upperPercent);
            }
        }
    }

    /**
     * Returns <code>true</code>, indicating that the domain axis/axes for this
     * plot are zoomable.
     * 
     * @return A boolean.
     * 
     * @see #isRangeZoomable()
     */
    public boolean isDomainZoomable() {
        return true;
    }

    /**
     * Returns <code>true</code>, indicating that the range axis/axes for this
     * plot are zoomable.
     * 
     * @return A boolean.
     * 
     * @see #isDomainZoomable()
     */
    public boolean isRangeZoomable() {
        return true;
    }

    /**
     * Returns the number of series in the primary dataset for this plot. If the
     * dataset is <code>null</code>, the method returns 0.
     * 
     * @return The series count.
     */
    public int getSeriesCount() {
        int result = 0;
        XYDataset dataset = getDataset();
        if (dataset != null) {
            result = dataset.getSeriesCount();
        }
        return result;
    }

    /**
     * Returns the fixed legend items, if any.
     * 
     * @return The legend items (possibly <code>null</code>).
     * 
     * @see #setFixedLegendItems(LegendItemCollection)
     */
    public LegendItemCollection getFixedLegendItems() {
        return this.fixedLegendItems;
    }

    /**
     * Sets the fixed legend items for the plot. Leave this set to
     * <code>null</code> if you prefer the legend items to be created
     * automatically.
     * 
     * @param items
     *            the legend items (<code>null</code> permitted).
     * 
     * @see #getFixedLegendItems()
     */
    public void setFixedLegendItems(LegendItemCollection items) {
        this.fixedLegendItems = items;
        fireChangeEvent();
    }

    /**
     * Returns the legend items for the plot. Each legend item is generated by
     * the plot's renderer, since the renderer is responsible for the visual
     * representation of the data.
     * 
     * @return The legend items.
     */
    public LegendItemCollection getLegendItems() {
        if (this.fixedLegendItems != null) {
            return this.fixedLegendItems;
        }
        LegendItemCollection result = new LegendItemCollection();
        int count = this.datasets.size();
        for (int datasetIndex = 0; datasetIndex < count; datasetIndex++) {
            XYDataset dataset = getDataset(datasetIndex);
            if (dataset != null) {
                XYItemRenderer renderer = getRenderer(datasetIndex);
                if (renderer == null) {
                    renderer = getRenderer(0);
                }
                if (renderer != null) {
                    int seriesCount = dataset.getSeriesCount();
                    for (int i = 0; i < seriesCount; i++) {
                        if (renderer.isSeriesVisible(i)
                                && renderer.isSeriesVisibleInLegend(i)) {
                            LegendItem item = renderer.getLegendItem(
                                    datasetIndex, i);
                            if (item != null) {
                                result.add(item);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns <code>true</code>, indicating that the domain axis/axes for this
     * plot are movable.
     * 
     * @return A boolean.
     * 
     * @see #isRangeMovable()
     */
    public boolean isDomainMovable() {
        return true;
    }

    /**
     * Returns <code>true</code>, indicating that the range axis/axes for this
     * plot are movable.
     * 
     * @return A boolean.
     * 
     * @see #isDomainMovable()
     */
    public boolean isRangeMovable() {
        return true;
    }

    /**
     * Move on the domain axis/axes. The new bounds are specified as percentages
     * of the current axis range.
     * 
     * @param movePercent
     *            a percentage that determines the new bound for the axis
     * @param info
     *            Deletion schedule.
     * @param source
     *            Deletion schedule.
     */
    public void moveDomainAxes(double movePercent, PlotRenderingInfo info, PointF source) {
        for (int i = 0; i < this.domainAxes.size(); i++) {
            ValueAxis domainAxis = (ValueAxis) this.domainAxes.get(i);
            if (domainAxis != null) {
                domainAxis.moveRange(movePercent);
            }
        }
    }

    /**
     * Move on the range axis/axes. The new bounds are specified as percentages
     * of the current axis range.
     * 
     * @param movePercent
     *            a percentage that determines the new bound for the axis
     * @param info
     *            Deletion schedule.
     * @param source
     *            Deletion schedule.
     */
    public void moveRangeAxes(double movePercent, PlotRenderingInfo info, PointF source) {
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            ValueAxis rangeAxis = (ValueAxis) this.rangeAxes.get(i);
            if (rangeAxis != null) {
                rangeAxis.moveRange(movePercent);
            }
        }
    }

    /**
     * Returns the effect for the grid-lines (if any) plotted against the domain
     * axis.
     * 
     * @return The effect (never <code>null</code>).
     * 
     * @see #setDomainGridlineEffect(PathEffect domainGridlineEffect)
     */
    public PathEffect getDomainGridlineEffect() {
        return domainGridlineEffect;
    }

    /**
     * Sets the effect for the grid lines plotted against the domain axis, and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param effect
     *            the effect (<code>null</code> not permitted).
     * 
     * @throws IllegalArgumentException
     *             if <code>effect</code> is <code>null</code>.
     * 
     * @see #getDomainGridlineEffect()
     */
    public void setDomainGridlineEffect(PathEffect domainGridlineEffect) {
        this.domainGridlineEffect = domainGridlineEffect;
        fireChangeEvent();
    }

    /**
     * Returns the effect for the grid lines (if any) plotted against the range
     * axis.
     * 
     * @return The effect (never <code>null</code>).
     * 
     * @see #setRangeGridlineEffect(PathEffect rangeGridlineEffect) {
     */
    public PathEffect getRangeGridlineEffect() {
        return rangeGridlineEffect;
    }

    /**
     * Sets the effect for the grid lines plotted against the range axis, and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param effect
     *            the effect (<code>null</code> not permitted).
     * 
     * @see #getRangeGridlineEffect()
     */
    public void setRangeGridlineEffect(PathEffect rangeGridlineEffect) {
        this.rangeGridlineEffect = rangeGridlineEffect;
        fireChangeEvent();
    }

    /**
     * Returns the effect for the minor grid-lines (if any) plotted against the
     * domain axis.
     * 
     * @return The effect (never <code>null</code>).
     * 
     * @see #setDomainMinorGridlineEffect(PathEffect domainMinorGridlineEffect)
     * 
     */
    public PathEffect getDomainMinorGridlineEffect() {
        return domainMinorGridlineEffect;
    }

    /**
     * Sets the effect for the minor grid lines plotted against the domain axis,
     * and sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param effect
     *            the effect (<code>null</code> not permitted).
     * 
     * @see #getDomainMinorGridlineEffect()
     * 
     */
    public void setDomainMinorGridlineEffect(PathEffect domainMinorGridlineEffect) {
        this.domainMinorGridlineEffect = domainMinorGridlineEffect;
        fireChangeEvent();
    }

    /**
     * Returns the effect for the minor grid lines (if any) plotted against the
     * range axis.
     * 
     * @return The effect (never <code>null</code>).
     * 
     * @see #setRangeMinorGridlineEffect(PathEffect rangeMinorGridlineEffect)
     * 
     */
    public PathEffect getRangeMinorGridlineEffect() {
        return rangeMinorGridlineEffect;
    }

    /**
     * Sets the effect for the minor grid lines plotted against the range axis,
     * and sends a {@link PlotChangeEvent} to all registered listeners.
     * 
     * @param effect
     *            the effect (<code>null</code> not permitted).
     * 
     * @see #getRangeMinorGridlineEffect()
     * 
     */
    public void setRangeMinorGridlineEffect(PathEffect rangeMinorGridlineEffect) {
        this.rangeMinorGridlineEffect = rangeMinorGridlineEffect;
        fireChangeEvent();
    }
    
    
}