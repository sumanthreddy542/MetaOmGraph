package edu.iastate.metnet.metaomgraph.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.iastate.metnet.metaomgraph.ui.ColorChooseButton;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author sumanth
 *
 */
public class HeatMapColorChoserDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private ColorChooseButton minValueColorButton;
	private ColorChooseButton maxValueColorButton;
	private Color minValueColor;
	private Color maxValueColor;
	private boolean colorsChanged;

	/**
	 * Create the dialog.
	 */
	public HeatMapColorChoserDialog(Color minValueColor, Color maxValueColor) {
		this.minValueColor = minValueColor;
		this.maxValueColor = maxValueColor;
		setBounds(100, 100, 450, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		colorsChanged = false;
		minValueColorButton = new ColorChooseButton(minValueColor, "Min value color");
		
		minValueColorButton.addChangeListener(new ChangeListener() {		
			@Override
			public void stateChanged(ChangeEvent e) {
				colorsChanged = true;
			}
		});
		
		maxValueColorButton = new ColorChooseButton(maxValueColor, "Max value color");
		maxValueColorButton.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				colorsChanged = true;
			}
		});
		
		{
			JLabel lblMinValueRange = new JLabel("Min value color:");
			GridBagConstraints gbc_lblMinValueRange = new GridBagConstraints();
			gbc_lblMinValueRange.insets = new Insets(0, 0, 5, 5);
			gbc_lblMinValueRange.gridx = 1;
			gbc_lblMinValueRange.gridy = 3;
			contentPanel.add(lblMinValueRange, gbc_lblMinValueRange);
			
			GridBagConstraints minButtonConstraints = new GridBagConstraints();
			minButtonConstraints.insets = new Insets(0, 0, 5, 0);
			minButtonConstraints.gridx = 2;
			minButtonConstraints.gridy = 3;
			minButtonConstraints.fill = GridBagConstraints.BOTH;
			contentPanel.add(minValueColorButton, minButtonConstraints);
		}
		{
			JLabel lblMaxValueRange = new JLabel("Max value color:");
			GridBagConstraints gbc_lblMaxValueRange = new GridBagConstraints();
			gbc_lblMaxValueRange.gridx = 1;
			gbc_lblMaxValueRange.gridy = 5;
			contentPanel.add(lblMaxValueRange, gbc_lblMaxValueRange);
			
			GridBagConstraints maxButtonConstraints = new GridBagConstraints();
			maxButtonConstraints.insets = new Insets(0, 0, 5, 0);
			maxButtonConstraints.gridx = 2;
			maxButtonConstraints.gridy = 5;
			maxButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
			contentPanel.add(maxValueColorButton, maxButtonConstraints);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(this);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public boolean isColorsUpdated() {
		return colorsChanged;
	}
	
	public Color getMinValueColor() {
		return minValueColor;
	}
	
	public Color getMaxValueColor() {
		return maxValueColor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if("OK".equals(e.getActionCommand())) {
			minValueColor = minValueColorButton.getColor();
			maxValueColor = maxValueColorButton.getColor();
			dispose();
		}
		
		if("Cancel".equals(e.getActionCommand())) {
			colorsChanged = false;
			dispose();
		}
	}
}
