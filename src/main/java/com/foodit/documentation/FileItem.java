package com.foodit.documentation;

public class FileItem implements Comparable<FileItem> {
	public String folder;
	public String method;

	public FileItem(String folder, String method) {
		this.folder = folder;
		this.method = method;

	}

	@Override
	public int compareTo(FileItem f2) {
		if (this.folder.compareTo(f2.folder) != 0) {
			return this.folder.compareTo(f2.folder);
		} else {
			return this.method.compareTo(f2.method);
		}
	}

}
