package kr.ac.sbs.command;

import com.jsp.dto.PdsVO;

public class PdsModifyCommand extends PdsRegistCommand{
	//field가 아에 똑같아도 새로 확장해서 구현하는 편이 유지보수측면에서도 훨씬 좋음.
	private int pno;
	private String[] deleteFile; //int타입으로 변환해도 상관없음
	
	public int getPno() {
		return pno;
	}
	public void setPno(int pno) {
		this.pno = pno;
	}
	public String[] getDeleteFile() {
		return deleteFile;
	}
	public void setDeleteFile(String[] deleteFile) {
		this.deleteFile = deleteFile;
	}
	@Override
	public PdsVO toPdsVO() {
		
		PdsVO pds = super.toPdsVO();
		pds.setPno(pno);
		return pds;
	}
	
	
	
}
