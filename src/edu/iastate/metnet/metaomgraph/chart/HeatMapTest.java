/**
 * 
 */
package edu.iastate.metnet.metaomgraph.chart;

import edu.iastate.metnet.metaomgraph.FrameModel;
import edu.iastate.metnet.metaomgraph.IconTheme;
import edu.iastate.metnet.metaomgraph.MetaOmGraph;
import edu.iastate.metnet.metaomgraph.ui.CustomFileSaveDialog;
import edu.iastate.metnet.metaomgraph.ui.CustomMessagePane;
import edu.iastate.metnet.metaomgraph.ui.CustomMessagePane.MessageBoxButtons;
import edu.iastate.metnet.metaomgraph.ui.CustomMessagePane.MessageBoxType;
import edu.iastate.metnet.metaomgraph.ui.TaskbarInternalFrame;
import edu.iastate.metnet.metaomgraph.utils.Utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.filefilter.FileFileFilter;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.style.AxesChartStyler.TextAlignment;
import org.knowm.xchart.style.Styler.LegendLayout;
import org.sing_group.gc4s.dialog.ColorsSelectionDialog;
import org.sing_group.gc4s.dialog.FontConfigurationDialog;
import org.sing_group.gc4s.input.DoubleRange;
import org.sing_group.gc4s.input.list.ColorsListPanel;
import org.sing_group.gc4s.visualization.heatmap.JHeatMap;
import org.sing_group.gc4s.visualization.heatmap.JHeatMapModel;
import org.sing_group.gc4s.visualization.heatmap.JHeatMapPanel;

/**
 * @author sumanth
 *
 */
public class HeatMapTest extends TaskbarInternalFrame implements ActionListener{
	
	private double[][] originalData;
	private double[][] heatMapData;
	private String[] rowNames;
	private String[] columnNames;
	private String[] selectedDataCols;
	
	private JButton properties;
	private JButton save;
	private JButton print;
	private JButton zoomIn;
	private JButton zoomOut;
	private JButton defaultZoom;
	private JButton changePalette;
	private JButton splitDataset;
	private JToggleButton toggleLegend;
	private boolean legendFlag = true;
	
	private JScrollPane scrollPane;
	
	private JButton bottomPanelButton;
	
	private HeatMapChart heatMapChart;
	private Color minValueColor = Color.GREEN;
	private Color maxValueColor = Color.RED;
	private double minValue;
	private double maxValue;
	private boolean transposeData;
	private boolean sampleDataOnRow;
	private String splitCol;
	private Map<String, Collection<Integer>> splitIndex;
	
	public HeatMapTest(double[][] data, String[] rowNames, String[] columnNames, boolean transposeData, boolean test) {
		this.heatMapData = data;
		this.rowNames = rowNames;
		this.columnNames = columnNames;
		createHeatMapPanelTest();
	}
	
	public HeatMapTest(double[][] data, String[] rowNames, String[] columnNames, boolean transposeData) {
		super();
		this.originalData = data;
		this.heatMapData = data;
		this.rowNames = rowNames;
		this.columnNames = columnNames;
		this.transposeData = transposeData;
		this.sampleDataOnRow = transposeData;
		this.selectedDataCols = columnNames;
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		createTopToolBar();
		
		scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		createHeatMapPanel();
		
		createBottomPanel();
	
		this.setClosable(true);
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setTitle("Heat Map");
		
		FrameModel heatMapFrameModel = new FrameModel("Heat Map", "Heat Map", 3);
		setModel(heatMapFrameModel);
	}
	
	private void createTopToolBar() {
		JPanel chartButtonsPanel = new JPanel();
		chartButtonsPanel.setLayout(new FlowLayout());

		IconTheme theme = MetaOmGraph.getIconTheme();
		properties = new JButton(theme.getProperties());
		properties.setToolTipText("Chart Properties");
		properties.setActionCommand("Properties");
		properties.addActionListener(this);
		save = new JButton(theme.getSaveAs());
		save.setToolTipText("Save Chart as Image");
		print = new JButton(theme.getPrint());
		print.setToolTipText("Print Chart");
		zoomIn = new JButton(theme.getZoomIn());
		zoomIn.setToolTipText("Zoom In");
		zoomOut = new JButton(theme.getZoomOut());
		zoomOut.setToolTipText("Zoom Out");
		defaultZoom = new JButton(theme.getDefaultZoom());
		defaultZoom.setToolTipText("Default Zoom");	
		save.setActionCommand("SaveImage");
		save.addActionListener(this);
		print.setActionCommand("PRINT");
		print.addActionListener(this);
		zoomIn.setActionCommand("ZoomIn");
		zoomIn.addActionListener(this);
		zoomOut.setActionCommand("ZoomOut");
		zoomOut.addActionListener(this);
		defaultZoom.setActionCommand("DefaultZoom");
		defaultZoom.addActionListener(this);

		splitDataset = new JButton(theme.getSort());
		splitDataset.setToolTipText("Split by categories");
		splitDataset.setActionCommand("splitDataset");
		splitDataset.addActionListener(this);

		changePalette = new JButton(theme.getPalette());
		changePalette.setToolTipText("Color Palette");
		changePalette.setActionCommand("changePalette");
		changePalette.addActionListener(this);
		changePalette.setOpaque(false);
		changePalette.setContentAreaFilled(false);
		changePalette.setBorderPainted(true);

		toggleLegend = new JToggleButton(theme.getLegend(), legendFlag);
		toggleLegend.setToolTipText("Show/hide legend");
		toggleLegend.setActionCommand("legend");
		toggleLegend.addActionListener(this);

		chartButtonsPanel.add(properties);
		chartButtonsPanel.add(save);
		chartButtonsPanel.add(zoomIn);
		chartButtonsPanel.add(zoomOut);
		chartButtonsPanel.add(defaultZoom);
		chartButtonsPanel.add(splitDataset);
		chartButtonsPanel.add(changePalette);

		getContentPane().add(chartButtonsPanel, BorderLayout.NORTH);
	}
	
	private void createBottomPanel() {
		JPanel bottomPanel = new JPanel();

		bottomPanelButton = new JButton("Transpose axis");
		bottomPanelButton.setActionCommand("transposeAxis");
		bottomPanelButton.addActionListener(this);
		bottomPanel.add(bottomPanelButton);
		
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private List<Number[]> arrayToListTranspose(){
		List<Number[]> list = new ArrayList<>(heatMapData.length);
		for (int i = 0; i < heatMapData.length; i++) {
			for (int j = 0; j < heatMapData[0].length; j++) {
				{
					Number[] numbers = new Number[3];
					numbers[0] = i;
					numbers[1] = j;
					numbers[2] = heatMapData[i][j];
					list.add(numbers);
				}
			}
		}
		return list;
	}
	
	private List<Number[]> arrayToList() {
		List<Number[]> list = new ArrayList<>(heatMapData.length);
		for (int i = 0; i < heatMapData.length; i++) {
			for (int j = 0; j < heatMapData[0].length; j++) {
				{
					Number[] numbers = new Number[3];
					numbers[0] = j;
					numbers[1] = i;
					numbers[2] = heatMapData[i][j];
					list.add(numbers);
				}
			}
		}
		return list;
	}
	
	private void createHeatMapPanel() {
		
		heatMapChart = new HeatMapChartBuilder().title("HeatMap").build();
	    heatMapChart.getStyler().setShowValue(false);
	    Color[] rangeColors = {Color.CYAN, Color.YELLOW};
	    heatMapChart.getStyler().setRangeColors(rangeColors);
	    heatMapChart.getStyler().setToolTipsEnabled(true);
	    heatMapChart.getStyler().setXAxisLabelRotation(270);
	    if(transposeData) {
	    	heatMapChart.setXAxisTitle("Genes");
	    	heatMapChart.setYAxisTitle("Samples");
	    	List<Number[]> numberData = arrayToListTranspose();
	    	heatMapChart.addSeries("heatmap", Arrays.asList(rowNames), Arrays.asList(columnNames), numberData);
	    }
	    else {
	    	heatMapChart.setXAxisTitle("Samples");
	    	heatMapChart.setYAxisTitle("Genes");
	    	List<Number[]> numberData = arrayToList();
	    	heatMapChart.addSeries("heatmap", Arrays.asList(columnNames), Arrays.asList(rowNames), numberData);
	    }
	    XChartPanel<HeatMapChart> chartPanel = new XChartPanel<HeatMapChart>(heatMapChart);
		maxValue = heatMapChart.getHeatMapSeries().getMax();
		minValue = heatMapChart.getHeatMapSeries().getMin();
	    scrollPane.setViewportView(chartPanel);
	}
	
	private void createHeatMapPanelTest() {
//		JHeatMapModel heatMapModel = new JHeatMapModel(heatMapData, rowNames, columnNames);
//		heatMap = new JHeatMap(heatMapModel);
//		heatMap.setPreferredSize(new Dimension(800, 600));
//		JHeatMapPanel heatMapPanel = new JHeatMapPanel(heatMap);
//		
//		getContentPane().add(heatMapPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if("transposeAxis".equals(e.getActionCommand())) {
			transposeData = !transposeData;
			sampleDataOnRow = !sampleDataOnRow;
			createHeatMapPanel();
		}
		
		if("Properties".equals(e.getActionCommand())) {
			HeatMapChartProperties properties = new HeatMapChartProperties(minValue, maxValue);
			properties.setModal(true);
			properties.setVisible(true);
			
			if(properties.isChartPropertiesUpdated()) {
				minValue = properties.getMinValue();
				maxValue = properties.getMaxValue();
				heatMapChart.getHeatMapSeries().setMin(minValue);
				heatMapChart.getHeatMapSeries().setMax(maxValue);
			}
		}
		
		if("ZoomIn".equals(e.getActionCommand())) {
//			try {
//				heatMapChart.getStyler().set
//				heatMap.zoomIn(2.0);
//			}
//			catch(Exception e1) {
//				CustomMessagePane errorMessage = new CustomMessagePane("Heat Map zoom", "Reached the maximum zoom limit",
//						MessageBoxType.ERROR, MessageBoxButtons.OK);
//				errorMessage.displayMessageBox();
//			}

		}
		
		if("ZoomOut".equals(e.getActionCommand())) {
//			try {
//				heatMap.zoomOut(0.5);
//			}
//			catch(Exception e1) {
//				CustomMessagePane errorMessage = new CustomMessagePane("Heat Map zoom", "Reached the minimum zoom limit",
//						MessageBoxType.ERROR, MessageBoxButtons.OK);
//				errorMessage.displayMessageBox();
//			}
		}
		
		if("DefaultZoom".equals(e.getActionCommand())) {
			createHeatMapPanel();
		}
		
		if("SaveImage".equals(e.getActionCommand())){
			HashMap<String, String> fileTypes = new HashMap<String, String>();
			fileTypes.put("PNG", ".png");
			JFileChooser fileSave = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG files", "png");
			fileSave.addChoosableFileFilter(filter);
			
			if(fileSave.showSaveDialog(MetaOmGraph.getMainWindow()) == JFileChooser.APPROVE_OPTION){
				
				File fileToSave = fileSave.getSelectedFile();
				if (!fileToSave.getName().toLowerCase().endsWith(".png")) {
					fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".png");
			      }
				try {
					BitmapEncoder.saveBitmap(heatMapChart, fileToSave.getAbsolutePath(), BitmapFormat.PNG);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}				
		}
		
		if("changePalette".equals(e.getActionCommand())) {
			HeatMapColorChoserDialog colorChoserDialog = new HeatMapColorChoserDialog(minValueColor, maxValueColor);
			colorChoserDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			colorChoserDialog.setModal(true);
			colorChoserDialog.setVisible(true);
			
			if(colorChoserDialog.isColorsUpdated()) {
				minValueColor = colorChoserDialog.getMinValueColor();
				maxValueColor = colorChoserDialog.getMaxValueColor();
				Color[] rangeColors = {minValueColor, maxValueColor};
				heatMapChart.getStyler().setRangeColors(rangeColors);
			}
		}
		
		if("splitDataset".equals(e.getActionCommand())) {
			String[] options = {"By Metadata", "By Query", "Reset"};	

			String col_val = (String) JOptionPane.showInputDialog(null, "Choose the column:\n", "Please choose",
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (col_val == null) {
				return;
			}
			
			if (col_val.equals("Reset")) {
				splitCol = null;
				splitIndex = null;
				this.heatMapData = originalData;
				createHeatMapPanel();
				return;
			}
			List<String> selectedVals = new ArrayList<>();
			if (col_val.equals("By Metadata")) {
				String[] fields =  MetaOmGraph.getActiveProject().getMetadataHybrid().getMetadataHeaders();
				JCheckBox[] cBoxes = new JCheckBox[fields.length];
				JPanel cbPanel = new JPanel();
				cbPanel.setLayout(new GridLayout(0, 3));
				for (int i = 0; i < fields.length; i++) {
					cBoxes[i] = new JCheckBox(fields[i]);
					cbPanel.add(cBoxes[i]);
				}
				int res = JOptionPane.showConfirmDialog(null, cbPanel, "Select categories",
						JOptionPane.OK_CANCEL_OPTION);
				if (res == JOptionPane.OK_OPTION) {
					for (int i = 0; i < fields.length; i++) {
						if (cBoxes[i].isSelected()) {
							selectedVals.add(fields[i]);
						}
					}
					splitCol = col_val;
				} else {
					return;
				}
				List<String> dataCols = Arrays.asList(selectedDataCols);
				splitIndex = MetaOmGraph.getActiveProject().getMetadataHybrid().cluster(selectedVals, dataCols);
				updateHeatMap();
			}
		}
	}
	
	private void updateHeatMap() {
		if(splitIndex != null) {
			if(sampleDataOnRow) {
				double[][] updatedData = new double[rowNames.length + splitIndex.size()][columnNames.length];
				String[] updatedRows = new String[rowNames.length + splitIndex.size()];
				int index = 0;
				for(Map.Entry<String, Collection<Integer>> entry : splitIndex.entrySet()) {
					updatedRows[index] = entry.getKey();
					double[] naArray = new double[columnNames.length];
					Arrays.fill(naArray, Double.NaN);
					naArray[naArray.length-1] = minValue;
					updatedData[index++] = naArray;
					ArrayList<Integer> clusterIndices = new ArrayList<Integer>(entry.getValue());
					for(int i : clusterIndices) {
						updatedRows[index] = rowNames[i];
						updatedData[index++] = heatMapData[i];
					}
				}
				
//				JHeatMapModel heatMapModel = new JHeatMapModel(updatedData, updatedRows, columnNames);
//				heatMap = new JHeatMap(heatMapModel);
//				heatMap.setNanColor(Color.WHITE);
//				heatMap.setPreferredSize(new Dimension(800, 600));
//				maxValue = heatMap.getHighValue();
//				minValue = heatMap.getLowValue();
//				scrollPane.setViewportView(heatMap);
			}
			else {
				heatMapChart = new HeatMapChartBuilder().build();
				heatMapChart.getStyler().setPlotContentSize(1);
			    heatMapChart.getStyler().setShowValue(false);
			    Color[] rangeColors = {Color.GREEN, Color.RED};
			    heatMapChart.getStyler().setRangeColors(rangeColors);
			    heatMapChart.getStyler().setToolTipsEnabled(true);
				for(Map.Entry<String, Collection<Integer>> entry : splitIndex.entrySet()) {
					String seriesName = entry.getKey();
					List<Number[]> columnData = new ArrayList<Number[]>();
					List<String> columns = new ArrayList<String>();
					ArrayList<Integer> clusterIndices = new ArrayList<Integer>(entry.getValue());
					int colIndex = 0;
					for(int i : clusterIndices) {
						columns.add(columnNames[i]);
						for(int j = 0; j < heatMapData.length; j++) {
							{
								Number[] numbers = new Number[3];
								numbers[0] = j;
								numbers[1] = colIndex++;
								numbers[2] = heatMapData[j][i];
								columnData.add(numbers);
							}
						}
					}	
					heatMapChart.addSeries(seriesName, columns, Arrays.asList(rowNames), columnData);
				}
				XChartPanel<HeatMapChart> chartPanel = new XChartPanel<HeatMapChart>(heatMapChart);
				scrollPane.setViewportView(chartPanel);
//				JHeatMapModel heatMapModel = new JHeatMapModel(updatedData, rowNames, updatedColumns);
//				heatMap = new JHeatMap(heatMapModel);
//				heatMap.setNanColor(Color.WHITE);
//				heatMap.setPreferredSize(new Dimension(800, 600));
//				maxValue = heatMap.getHighValue();
//				minValue = heatMap.getLowValue();
//				scrollPane.setViewportView(heatMap);
			}
		}
	}
}
