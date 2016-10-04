package usb_icon_creator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JProgressBar;

public class USBIconProgram_V2 extends JFrame {
	
	WorkingThread wt;
	
	private static final long serialVersionUID = 2607324722318246399L;
	private JPanel contentPane;
	private final int frameH = 350;
	private final int frameW = 425;
	
	private FileSystemView fsv = FileSystemView.getFileSystemView();
	private File[] fr = File.listRoots();
	private File[] dirs = getDirs();
	
	private JLabel header1;
	private JLabel devL;
	private JLabel devTSl;
	private JLabel devFSl;
	private JLabel devType1;
	private JLabel iconLabel;
	private JLabel devLabel;
	private JLabel schLabel;
	private JLabel outputLabel;
	private JLabel nameLabel;
	private JLabel iconLabel2;
	private JLabel label_1;
	
	private JTextField devLetter;
	private JTextField devTS;
	private JTextField devFS;
	private JTextField devType;
	private JTextField labelTF2;
	private JTextField devLabel1;
	private JTextField iconTF;
	private JTextField iconTF2;
	
	private JButton finishButton;
	private JButton fileBrowserBt;
	private JRadioButton rb_search2;
	private JRadioButton rb_search1;
	private JFileChooser jFileChooser1;
	private ButtonGroup searchRBG;
	private JProgressBar pbar;

	private static int adjX = -70; // Ajuste das parcelas em X
	private static int adjY = 29; // Ajuste das parcelas em Y
	private static DefaultComboBoxModel<FileType> model;
	private String dir = null;
	private String filename = "Autorun.inf";
	private String iconPath = null;
	private String iconName = null;
	private String label = null;
	private String picsDir = null;


	public static void main(String[] args) {
		model = new DefaultComboBoxModel<FileType>();
		USBIconProgram_V2 frame = new USBIconProgram_V2();
		frame.setVisible(true);
		frame.run();
	}
	
	public void run() {
		wt = new WorkingThread();
		wt.start();
	}

	public USBIconProgram_V2() {
		setTitle("USB Autorun Creator by Chief_Bugs");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, frameW, frameH);
		//Fechar programa quando clica na cruz. Assim para o main e não só a janela.
		addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	            System.exit(0);
	        }
	    });
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		// Layout da GUI
		layout_GUI();
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	private void layout_GUI() {
		try {		
			fileBrowserBt = new JButton();
			contentPane.add(fileBrowserBt);
			fileBrowserBt.setText("Browse...");
			fileBrowserBt.setBounds(60, 144, 90, 22);
			fileBrowserBt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					openFileBrowser();
				}
			});
			
			header1 = new JLabel();
			contentPane.add(header1);
			header1.setText("Device");
			header1.setBounds(12, 12, 60, 15);
			
			JComboBox<FileType> selDev = new JComboBox<FileType>(model);
			model.addElement(new FileType("Select Device..."));
			for (File d : dirs) {
				model.addElement(new FileType(d.getPath()));
			}
			
			contentPane.add(selDev);
			selDev.setBounds(60, 8, 150, 22);
			selDev.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String a = model.getSelectedItem().toString();
					devLetter.setText("");
					devTS.setText("");
					devFS.setText("");
					devType.setText("");

					if (!a.contains("Select Device")) {
						showDirDetails(a);
						dir = a;
						
						if (rb_search1.isSelected())
							picsDir = dir;
						
						finishButton.setEnabled(true);
					} else finishButton.setEnabled(false);
				}
			});

			devL = new JLabel();
			contentPane.add(devL);
			devL.setText("Device");
			devL.setBounds(313 + adjX, 10 + adjY * 0, 73, 22);

			devTSl = new JLabel();
			contentPane.add(devTSl);
			devTSl.setText("Total Space");
			devTSl.setBounds(285 + adjX, 10 + adjY * 1, 73, 22);

			devFSl = new JLabel();
			contentPane.add(devFSl);
			devFSl.setText("Free Space");
			devFSl.setBounds(288 + adjX, 10 + adjY * 2, 73, 22);

			devType1 = new JLabel();
			contentPane.add(devType1);
			devType1.setText("Type");
			devType1.setBounds(323 + adjX, 10 + adjY *3, 73, 22);

			iconLabel = new JLabel();
			contentPane.add(iconLabel);
			iconLabel.setText("Icon");
			iconLabel.setBounds(20, 147, 30, 15);

			devLabel = new JLabel();
			contentPane.add(devLabel);
			devLabel.setText("Device Label");
			devLabel.setBounds(20, 208, 73, 15);	

			devLetter = new JTextField();
			contentPane.add(devLetter);
			devLetter.setBounds(358 + adjX, 12 + adjY * 0, 123, 22);
			devLetter.setEditable(false);

			devTS = new JTextField();
			contentPane.add(devTS);
			devTS.setBounds(358 + adjX, 12 + adjY * 1, 123, 22);
			devTS.setEditable(false);

			devFS = new JTextField();
			contentPane.add(devFS);
			devFS.setBounds(358 + adjX, 12 + adjY * 2, 123, 22);
			devFS.setEditable(false);

			devType = new JTextField();
			contentPane.add(devType);
			devType.setBounds(358 + adjX, 12 + adjY * 3, 123, 22);
			devType.setEditable(false);

			iconTF = new JTextField();
			contentPane.add(iconTF);
			iconTF.setBounds(20, 174, 391, 22);
			iconTF.setEditable(false);

			devLabel1 = new JTextField();
			contentPane.add(devLabel1);
			devLabel1.setBounds(99, 206, 189, 22);
			devLabel1.getDocument().addDocumentListener(new DocumentListener() {
				public void removeUpdate(DocumentEvent arg0) {
					label = devLabel1.getText();
					labelTF2.setText(label);
					//System.out.println(label);
				}

				public void insertUpdate(DocumentEvent arg0) {
					label = devLabel1.getText();
					labelTF2.setText(label);
					//System.out.println(label);
				}

				public void changedUpdate(DocumentEvent arg0) {}
			});

			schLabel = new JLabel();
			contentPane.add(schLabel);
			schLabel.setText("Search on:");
			schLabel.setBounds(20, 70, 73, 15);	

			rb_search1 = new JRadioButton();
			contentPane.add(rb_search1);
			rb_search1.setText("Selected Device");
			rb_search1.setBounds(20, 91, 120, 19);
			rb_search1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					picsDir = dir;
				}
			});

			rb_search2 = new JRadioButton();
			contentPane.add(rb_search2);
			rb_search2.setText("User Pictures");
			rb_search2.setBounds(20, 117, 120, 19);
			rb_search2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					picsDir = System.getProperty("user.home") + "//Pictures";
				}
			});

			searchRBG = new ButtonGroup();
			searchRBG.add(rb_search1);
			searchRBG.add(rb_search2);

			outputLabel = new JLabel();
			contentPane.add(outputLabel);
			outputLabel.setText("OUTPUT");
			outputLabel.setBounds(20, 235, 60, 15);

			iconLabel2 = new JLabel();
			contentPane.add(iconLabel2);
			iconLabel2.setText("Icon");
			iconLabel2.setBounds(37, 255, 60, 15);

			nameLabel = new JLabel();
			contentPane.add(nameLabel);
			nameLabel.setText("Label");
			nameLabel.setBounds(30, 284, 60, 15);

			iconTF2 = new JTextField();
			contentPane.add(iconTF2);
			iconTF2.setBounds(70, 252, 126, 22);
			iconTF2.setEditable(false);

			labelTF2 = new JTextField();
			contentPane.add(labelTF2);
			labelTF2.setBounds(70, 281, 126, 22);
			labelTF2.setEditable(false);

			pbar = new JProgressBar();
			pbar.setBounds(320, 302, 90, 14);
			contentPane.add(pbar);
			
			finishButton = new JButton();
			contentPane.add(finishButton);
			finishButton.setText("Create");
			finishButton.setBounds(320, 280, 90, 20);
			finishButton.setEnabled(false);
			finishButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					pbar.setValue(0);
					pbar.setStringPainted(false);
					pbar.setIndeterminate(true);
					wt.setValues(dir, iconPath, iconName, label, pbar);
					wt.work();
				}
			});
		} catch (Exception e) {}
	}
	
	public void openFileBrowser() {
		{
			jFileChooser1 = new JFileChooser(picsDir);
			FileFilter ff = new FileNameExtensionFilter("Icons (ico, bmp, exe, dll)", "ico", "bmp", "exe", "dll");
			jFileChooser1.addChoosableFileFilter(ff);
			jFileChooser1.setFileFilter(ff);
			int j = jFileChooser1.showDialog(null, "Choose File");
			if ( j == JFileChooser.APPROVE_OPTION) {
				File temp = jFileChooser1.getSelectedFile();
				iconPath = temp.toString();
				iconName = temp.getName();
				iconTF.setText(iconPath);
				iconTF2.setText(iconName);
				//System.out.println(dir + iconName);
				label_1 = new JLabel("");
				label_1.setIcon(new ImageIcon(iconPath));
				label_1.setBounds(155, 100, 64, 64);
				contentPane.add(label_1);
			}
		}

	}
	
	public void showDirDetails(String a) {
		File f = null;
		boolean pass = false; 
		for (int i = 0; i < dirs.length; i++) {
			String l = dirs[i].getPath();
			if (l.contains(a)) {
				f = dirs[i];
				pass = true;
			}
		}
		if (pass) {
			String letter = f.getPath();
			String totalSpace = byteConv(f.getTotalSpace(), true);
			String freeSpace = byteConv(f.getFreeSpace(), true);
			String dType = fsv.getSystemTypeDescription(new File(letter));
			/*
			System.out.println("### " + letter + " ###\nTOTAL: " + totalSpace + "\nFREE: " + freeSpace
					+ "\nType: " + devType);
			*/
			
			devLetter.setText(letter);
			devTS.setText(totalSpace);
			devFS.setText(freeSpace);
			devType.setText(dType);
		}
	}
	
	public static String byteConv(long bytes, boolean si) {
	    int unit = si ? 1024 : 1000;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public File[] getDirs() {
		int numDirs = 0;
		for (int i = 0; i < fr.length; i++) {
			if (fsv.isDrive(fr[i]) && fr[i].canWrite()) {
				numDirs++;
			}
		}
		File[] dirs = new File[numDirs];
		int idx = 0;
		for (int i = 0; i < fr.length; i++) {
			if (fsv.isDrive(fr[i]) && fr[i].canWrite()) {
				dirs[idx] = fr[i];
				//System.out.println(dirs[idx]);
				idx++;
			}
		}
		
		return dirs;
	}
	
	public ArrayList<String> getIcons() {
		File folder = new File(dir);
		ArrayList<String> files = new ArrayList<String>();
		
		for (File file : folder.listFiles()) {
			if (file.isFile()) {
				String temp = file.getName();
				
				String ext = temp.substring(temp.lastIndexOf("."));
				if (ext.equals(".ico") || ext.equals(".exe") || ext.equals(".bmp")  || ext.equals(".dll")) {
					files.add(temp);
				}
			}
		}
		return files;
	}
	
	public void writeToFile() {
		File device = new File(dir);
		if (device.exists() && device.canWrite()) {
			PrintWriter writer = null;
			File txt = new File(dir + filename);
			txt.setWritable(true);
			
			try {
				writer = new PrintWriter(dir + filename, "UTF-8");
			} catch (IOException e) {}

			writer.println("[Autorun.Amd64]");
			writer.println("ICON= " + iconName + ",0");
			writer.println("LABEL= " + label);
			writer.println();
			writer.println("[Autorun]");
			writer.println("ICON= " + iconName + ",0");
			writer.println("LABEL= " + label);
			writer.println();
			writer.close();
			
			txt.setReadOnly();

			try {
				Files.copy(new File(iconPath).toPath(), new File(dir + iconName).toPath());
				File img = new File(dir + iconName);
				img.setReadOnly();
			} catch (IOException e) {}
			
		}
	}

	public void dormir(int m) {
		try {
			Thread.sleep(m);
		} catch(InterruptedException e) { }
	}
}
