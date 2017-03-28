package com.example.lzx.upfile;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class PersonBean extends BmobObject{
	
	private String Name;
	private String password;
	private BmobFile file;
	
	public PersonBean(String Name,String password,BmobFile file){
		this.Name = Name;
		this.password = password;
		this.file = file;
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public BmobFile getFile() {
		return file;
	}
	public void setFile(BmobFile file) {
		this.file = file;
	}
}
