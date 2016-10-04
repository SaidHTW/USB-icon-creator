package usb_icon_creator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import javax.swing.JProgressBar;

public class WorkingThread extends Thread {

	int state;
	final int IDLE = 0;
	final int WORKING = 1;

	private String dir, iconPath, iconName, label;
	private String filename = "Autorun.inf";
	JProgressBar pbar_done;

	public WorkingThread() {

	}

	public void setValues(String d, String ip, String in, String l, JProgressBar pbar) {
		dir = d;
		iconPath = ip;
		iconName = in;
		label = l;
		pbar_done = pbar;
	}

	@Override
	public void run() {
		state = IDLE;
		while(true) {
			switch (state) {
			case IDLE:
				sleep(1000);
				break;

			case WORKING:
				sleep(150);
				File device = new File(dir);
				if (device.exists() && device.canWrite()) {
					PrintWriter writer = null;
					File txt = new File(dir + filename);

					if (txt.exists()) {
						try {
							Files.setAttribute(txt.toPath(), "dos:hidden", Boolean.FALSE, LinkOption.NOFOLLOW_LINKS);
						} catch (IOException e2) {System.err.println("Cannot unhide file");}
						// try {
						// Files.delete(txt.toPath());
						// } catch (IOException e2) {System.err.println("Cannot delete existing file");}
					}
					
					try {
						txt.createNewFile();
					} catch (IOException e2) {System.err.println("Cannot create new file");}

					txt.setWritable(true);

					try {
						writer = new PrintWriter(new FileWriter(dir + filename));
					} catch (IOException e1) {
						System.err.println("Cannot start Writer");
					}

					try {
						writer.println("[Autorun.Amd64]");
						writer.println("ICON= " + iconName + ",0");
						writer.println("LABEL= " + label);
						writer.println();
						writer.println("[Autorun]");
						writer.println("ICON= " + iconName + ",0");
						writer.println("LABEL= " + label);
						writer.println();
						writer.close();
					} catch(NullPointerException e1) {
						System.err.println("Writer is null!");
					}


					txt.setReadOnly();
					try {
						Files.setAttribute(txt.toPath(), "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
					} catch (IOException e1) {
						System.err.println("Cannot hide file");
					}

					File img = new File(dir + iconName);
					if (!img.exists()) {
						try {
							Files.copy(new File(iconPath).toPath(), new File(dir + iconName).toPath());
							img.setReadOnly();
							Files.setAttribute(img.toPath(), "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
						} catch (IOException e) {}
					}

					pbar_done.setIndeterminate(false);
					pbar_done.setValue(100);
					pbar_done.setString("Done!");
					pbar_done.setStringPainted(true);
					idle();
				}
				break;
			}
		}
	}

	public void idle() {
		sleep(150);
		state = IDLE;
	}

	public void work() {
		state = WORKING;
	}

	public void sleep(int m) {
		try {
			Thread.sleep(m);
		} catch(InterruptedException e) { }
	}
}
