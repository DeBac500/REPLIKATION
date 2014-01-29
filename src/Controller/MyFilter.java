package Controller;

import java.io.File;
import java.io.FilenameFilter;

public class MyFilter implements FilenameFilter{

	@Override
	public boolean accept(File dir, String name) {
		return name.toLowerCase().endsWith(".pdf");
	}

}
