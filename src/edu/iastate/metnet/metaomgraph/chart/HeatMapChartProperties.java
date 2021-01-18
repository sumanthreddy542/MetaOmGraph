package edu.iastate.metnet.metaomgraph.chart;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.math3.util.Pair;
import org.sing_group.gc4s.dialog.FontConfigurationDialog;
import org.sing_group.gc4s.input.DoubleRangeInputPanel;
import org.sing_group.gc4s.input.FontConfigurationPanel;
import org.sing_group.gc4s.input.list.ColorsListPanel;
import org.sing_group.gc4s.visualization.heatmap.JHeatMap;

import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import edu.iastate.metnet.metaomgraph.MetaOmGraph;

import com.jgoodies.forms.layout.FormSpecs;

/**
 * 
 * @author sumanth
 *
 */
public class HeatMapChartProperties extends JDialog implements ActionListener {
	private JTextField minValueField;
	private JTextField maxValueField;
	private JHeatMap heatMap;
	private DoubleRangeInputPanel rangePanel;
	private FontConfigurationPanel fontPanel;
	private boolean chartPropertiesUpdated = false;
	private double minValue;
	private double maxValue;
	private Font font;

	public HeatMapChartProperties(double minValue, double maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		setBounds(100, 100, 560, 378);
		getContentPane().setLayout(null);

		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 310, 546, 31);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 27, 526, 273);
		getContentPane().add(tabbedPane);

		rangePanel = new DoubleRangeInputPanel(minValue, maxValue);
		tabbedPane.addTab("Range", null, rangePanel, null);

		fontPanel = new FontConfigurationPanel();
		tabbedPane.addTab("Font", null, fontPanel, null);
	}
	
	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}
	
	public Font getFont() {
		return font;
	}
	
	public boolean isChartPropertiesUpdated() {
		return chartPropertiesUpdated;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if("OK".equals(e.getActionCommand())) {
			chartPropertiesUpdated = true;
			minValue = rangePanel.getRange().getMin();
			maxValue = rangePanel.getRange().getMax();
			font = fontPanel.getSelectedFont(); 
			dispose();
		}
		
		if("Cancel".equals(e.getActionCommand())) {
			chartPropertiesUpdated = false;
			dispose();
		}
	}
}
