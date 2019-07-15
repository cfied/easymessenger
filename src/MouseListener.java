import java.awt.event.MouseEvent;

import javax.swing.JTextField;

public class MouseListener implements java.awt.event.MouseListener {
	private JTextField text;
	
	public MouseListener(JTextField text){
		this.text = text;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(text.getText().equals("Write a message")){
			text.setText("");
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
