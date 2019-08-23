//defines how messages are being displayed

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.SwingConstants;

public class MyCellRenderer extends DefaultListCellRenderer{
	
	String userId;
	ListModel<Message> listModel;
	
	public MyCellRenderer(String userId, ListModel<Message> listModel){
		super();
		this.userId = userId;
		this.listModel = listModel;
	}
	
	public Component getListCellRendererComponent(JList<?> list,
												  Object value,
												  int index,
												  boolean isSelected,
												  boolean cellHasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		//label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		
		label.setBounds(new Rectangle(20, label.getHeight()));
		
		if(userId.equals(((Message) listModel.getElementAt(index)).getSenderID())){
			label.setHorizontalAlignment(SwingConstants.RIGHT);
		}else{
			label.setHorizontalAlignment(SwingConstants.LEFT);
		}
		
		return label;
	}
}
