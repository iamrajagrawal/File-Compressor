import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
public class FileCompFrame extends JFrame{
private JTabbedPane tabbedPane = new JTabbedPane();
private FileCompComp  panCompression;

void centerWindow(){
Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();

setLocation((screensize.width / 2) - (getSize().width / 2),
(screensize.height / 2) - (getSize().height / 2));
}

FileCompFrame(String txtSource){
setVisible(false);


setTitle("File compressor");
setSize(425,390);
centerWindow();
setLayout(new BorderLayout(5,5));

panCompression = new FileCompComp(this,false,txtSource);

tabbedPane.addTab("Compressor/Decompressor",panCompression);



tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

getContentPane().add(tabbedPane,BorderLayout.CENTER);

setResizable(false);

setVisible(true);

}




protected void processWindowEvent(WindowEvent e) {

if (e.getID() == WindowEvent.WINDOW_CLOSING) {



int exit = JOptionPane.showConfirmDialog(this, "Are you sure?","Confirm Exit?",JOptionPane.YES_NO_OPTION);
if (exit == JOptionPane.YES_OPTION) {
setVisible(false);
}

} else {

super.processWindowEvent(e);
}
}

}

