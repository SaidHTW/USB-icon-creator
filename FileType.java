package usb_icon_creator;


public class FileType {

	private String p;

	public FileType(String path) {
		p = path;
	}
	
	@Override
	public String toString() {
		return p;
	}
}
