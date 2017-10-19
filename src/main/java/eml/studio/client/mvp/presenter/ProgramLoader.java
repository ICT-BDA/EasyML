package eml.studio.client.mvp.presenter;

import java.util.List;
import eml.studio.client.mvp.view.AdminView;
import eml.studio.client.rpc.CategoryService;
import eml.studio.client.rpc.CategoryServiceAsync;
import eml.studio.client.rpc.ProgramService;
import eml.studio.client.rpc.ProgramServiceAsync;
import eml.studio.client.ui.panel.AlertPanel;
import eml.studio.client.ui.panel.DeletePanel;
import eml.studio.client.util.Constants;
import eml.studio.client.util.Pagination;
import eml.studio.shared.model.Category;
import eml.studio.shared.model.Program;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * 后台管理-程序管理 内容加载
 * 
 * @author madongjing
 */
public class ProgramLoader {

  private final AdminView adminView;
  private ProgramServiceAsync programService = GWT.create(ProgramService.class);
  private CategoryServiceAsync categoryService = GWT.create(CategoryService.class);
  private DeletePanel deletePanel = new DeletePanel();					//面板：确认是否删除目录
  private AlertPanel alertPanel = new AlertPanel();						//提示框：操作成功or失败
  private AlertPanel searchAlert = new AlertPanel();					//提示框：搜索结果提示
  private final DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
  private int currentPage = 1;											//以下为页码相关信息
  private int pageSize;
  private int lastPage;
  private int resultSize;
  private int resultStart;
  private int everyPageSize;
  private Grid page = new Grid();										//分页控件放置区域
  private Pagination pagination;										//分页控件
  private Program searchProgram;										//以下为保存搜索输入的信息
  private String searchName;
  private String searchPath;
  private String searchStart = null;
  private String searchEnd = null;
  private String searchOwner;
  private boolean searchFlag = false;	//true表示搜索结果 false表示所有结果 用来区分分页控件要响应哪一个数据加载函数load()&reload()
 

  public ProgramLoader(AdminView adminView) {
	this.adminView = adminView;
	bind();
  }
  
  public void go(){
	  init();
  }
  
  /**
   * 事件绑定
   */
  public void bind(){
	//提示框-关闭
	alertPanel.getClose().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			History.fireCurrentHistoryState();
		}
						
	});
				
	//提示框-确认
	alertPanel.getConfirmBtn().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			History.fireCurrentHistoryState();
		}
						
	});
	
//	//搜索提示框-关闭
//	searchAlert.getClose().addClickHandler(new ClickHandler(){
//
//		@Override
//		public void onClick(ClickEvent event) {
//			// TODO Auto-generated method stub
//			//刷新当前页
//		}
//			
//	});
//	
//	//搜索提示框-确认
//	searchAlert.getConfirmBtn().addClickHandler(new ClickHandler(){
//
//		@Override
//		public void onClick(ClickEvent event) {
//			// TODO Auto-generated method stub
//			//刷新当前页
//		}
//		
//	});
	
	//搜索回车键响应
	adminView.getProgSearch().addDomHandler(new KeyUpHandler() {
    	@Override
        public void onKeyUp(KeyUpEvent event) {
    		if(event.getNativeKeyCode()== 13){
    			adminView.getProgSearchBtn().click();
        	}
        }
      }, KeyUpEvent.getType());
	
	//搜索按钮响应
	adminView.getProgSearchBtn().addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			//搜索前先清理上次搜索保存的searchProgram对象信息
			searchProgram = new Program();
			searchFlag = true;

			//获取此次搜索输入的信息 保存到searchProgram对象中
			searchName = adminView.getProgName().getValue();
			searchOwner = adminView.getProgOwner().getValue();
			searchPath = adminView.getProgCate().getValue();
			
			if(searchName != ""){
				searchProgram.setName(searchName);
			}else{
				searchProgram.setName(null);
			}
			if(searchOwner != ""){
				searchProgram.setOwner(searchOwner);
			}else{
				searchProgram.setOwner(null);
			}
			
			if( adminView.getProgStartDate().getValue() != null ){
				searchStart = formatter.format( adminView.getProgStartDate().getValue() );
			}else{
				searchStart = null;
			}
			if( adminView.getProgEndDate().getValue() != null ){
				searchEnd = formatter.format( adminView.getProgEndDate().getValue() );
			}else{
				searchEnd = null;
			}

			//若所有输入框均为空 则提示需要输入 若非均空则进行搜索
			if(searchName == "" && searchPath == "" && searchOwner == "" && searchStart == null && searchEnd == null){
				searchAlert.setContent( Constants.adminUIMsg.searchNoInput() );
				searchAlert.show();
			}else{
				//若searchPath为空 则直接搜索 非空则需要在搜索无结果时转换一下category path再搜索
				if(searchPath == ""){
					searchResult();
				}else{
					Category category = new Category();
					category.setPath(searchPath);
					categoryService.getCategory(category, "", new AsyncCallback<List<Category>>(){
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							searchAlert.setContent(caught.getMessage());
							searchAlert.show();
						}
		
						@Override
						public void onSuccess(List<Category> result) {
							// TODO Auto-generated method stub
						//查询category path是否存在 不存在时需要转换后再查询一下（因category根目录在数据库中是中英文混合保存的）
							if(result.size() == 0){
								Category tc = new Category();
								tc.setPath(changeCategory(searchPath));
								categoryService.getCategory(tc, "", new AsyncCallback<List<Category>>(){
		
									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										searchAlert.setContent(caught.getMessage());
										searchAlert.show();
									}
		
									@Override
									public void onSuccess(List<Category> result) {
										if(result.size() == 0){				//转换后再查询依然不存在 则提示目录出错
											searchAlert.setContent( Constants.adminUIMsg.searchCateError() );
											searchAlert.show();
										}else{								//转换后再查询存在 则获取category id 并搜索
											searchProgram.setCategory( result.get(0).getId() );
											searchResult();
										}
									}
								});
							}else{							//查询category path是否存在 存在则获取category id 并搜索
								searchProgram.setCategory( result.get(0).getId() );
								searchResult();
							}
						}
					});
				}
			}
		}
		
	});
	
	//页码点击响应
	page.addClickHandler(new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			int index = page.getCellForEvent(event).getCellIndex();
			String crtPageText = page.getText(0, index);
			currentPage = pagination.getCurrentPage(currentPage, crtPageText, lastPage);
			pagination.reload(currentPage);
			if(searchFlag){				//searchFlag=true 即显示搜索结果 调用reload()
				reload();
			}else						//searchFlag=false 即显示所有结果 调用load()
				load();
		}
		
	});
  }
  
  /**
   * 程序管理页初始化 数据初加载
   */
  private void init(){
	clearInput();											//清除输入框内容
	adminView.getProgPage().clear();						//清除分页区域内容 重新加载分页控件
	page.addStyleName("admin-page");
	adminView.getProgPage().add(page);
	currentPage = 1;
	
	programService.getSize(new AsyncCallback<Integer>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			caught.getMessage();
		}

		@Override
		public void onSuccess(Integer result) {
			// TODO Auto-generated method stub
			//计算页码相关信息 初始化并加载分页控件
			resultSize = result;
			pageSize = (int)Math.ceil((double)result/13);
			lastPage = pageSize;
			pagination = new Pagination(page, pageSize, Pagination.PageType.LARGE);
			pagination.load();
			//加载用户数据
			load();
		}
	});
  }
  
  /**
   * 加载数据 填入页面
   */
  private void load() {
	resultStart = (currentPage-1)*13;
	if(currentPage == lastPage){					//满页每页显示13条记录 尾页不足13条时计算一下实际条数
		everyPageSize = resultSize - resultStart;
	}else{
		everyPageSize = 13;
	}

	adminView.getProgGrid().resize(everyPageSize+1, 6);
	adminView.getProgGrid().setText(0, 0, "Name");
	adminView.getProgGrid().setText(0, 1, "Category");
	adminView.getProgGrid().setText(0, 2, "Description");
	adminView.getProgGrid().setText(0, 3, "CreateDate");
	adminView.getProgGrid().setText(0, 4, "Owner");
	adminView.getProgGrid().setText(0, 5, "Operation");

	programService.loadPart(resultStart, everyPageSize, new AsyncCallback<List<Program>>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			alertPanel.setContent(caught.getMessage());
			alertPanel.show();
		}

		@Override
		public void onSuccess(List<Program> result) {
			// TODO Auto-generated method stub
			for(int i=0;i<everyPageSize;i++){
				adminView.getProgGrid().setText(i+1, 0, result.get(i).getName());
				final String categoryId = result.get(i).getCategory();
				final int a = i;
				if(categoryId.length() == 0){
					adminView.getProgGrid().setText(a+1, 1, "————");
				}else if(("我的程序").equals(categoryId) || ("my program").equals(categoryId.toLowerCase())
					|| ("共享程序").equals(categoryId) || ("shared program").equals(categoryId.toLowerCase())
					|| ("系统程序").equals(categoryId) || ("system program").equals(categoryId.toLowerCase()) ){
					adminView.getProgGrid().setText(a+1, 1, categoryId);
				}else{
					categoryService.getCategory(categoryId, new AsyncCallback<Category>(){
	
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							adminView.getProgGrid().setText(a+1, 1, categoryId);
						}
	
						@Override
						public void onSuccess(Category result) {
							// TODO Auto-generated method stub
							if(result != null){
								adminView.getProgGrid().setText(a+1, 1, result.getPath());
							}else
								adminView.getProgGrid().setText(a+1, 1, "————");
						}
						
					});
				}
				if(result.get(i).getDescription().length() > 50){
					adminView.getProgGrid().setText(i+1, 2, result.get(i).getDescription().substring(0, 49)+"...");
				}else{
					adminView.getProgGrid().setText(i+1, 2, result.get(i).getDescription());
				}
				adminView.getProgGrid().setText(i+1, 3, result.get(i).getCreatedate());
				adminView.getProgGrid().setText(i+1, 4, result.get(i).getOwner());
				final String progId = result.get(i).getId();
				final String progName = result.get(i).getName();
				Label deleteProg = new Label();
				deleteProg.setTitle( Constants.adminUIMsg.deleteProg() );
				deleteProg.addStyleName("admin-user-delete");
				deleteProg.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						deletePanel.setContent( Constants.adminUIMsg.progDelete1() + progName + Constants.adminUIMsg.progDelete2() );
						deletePanel.show();
						deletePanel.getConfirmBtn().addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Program program = new Program();
								program.setId(progId);
								programService.delete(progId, new AsyncCallback<Void>(){

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										deletePanel.hide();
										alertPanel.setContent(caught.getMessage());
										alertPanel.show();
									}

									@Override
									public void onSuccess(Void result) {
										// TODO Auto-generated method stub
										deletePanel.hide();
										alertPanel.setContent( Constants.adminUIMsg.progSuccess() );
										alertPanel.show();
									}
									
								});
							}

						});
					}

				});
				HorizontalPanel operate = new HorizontalPanel();
				operate.addStyleName("admin-user");
				operate.add(deleteProg);
				adminView.getProgGrid().setWidget(i + 1, 5, operate);
			}
		}
	});
  }

  /**
   * 搜索 重置分页控件 加载搜索结果
   */	
  private void searchResult(){
	adminView.getProgPage().clear();							//清除分页区域内容 重新加载分页控
	adminView.getProgPage().add(page);
	programService.search(searchProgram, searchStart, searchEnd, 0, 0, new AsyncCallback<List<Program>>(){
	
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			searchAlert.setContent(caught.getMessage());
			searchAlert.show();
		}
	
		@Override
		public void onSuccess(List<Program> result) {
			// TODO Auto-generated method stub
			if(result.size() == 0){
				searchAlert.setContent( Constants.adminUIMsg.searchNoResult() );
				searchAlert.show();
			}else{
				resultSize = result.size();						//计算页码相关信息 重新设置分页控件 重新加载
				pageSize = (int)Math.ceil((double)result.size()/13);
				lastPage = pageSize;
				currentPage = 1;
				pagination = new Pagination(page, pageSize, Pagination.PageType.LARGE);
				pagination.load();
				reload();										//加载搜索结果
			}
		}
				
	});
  }
  
  /**
   * 加载搜索结果 填入页面
   */
  private void reload(){
	resultStart = (currentPage-1)*13;
	if(currentPage == lastPage){					//满页每页显示13条记录 尾页不足13条时计算一下实际条数
		everyPageSize = resultSize - resultStart;
	}else{
		everyPageSize = 13;
	}

	adminView.getProgGrid().resize(everyPageSize+1, 6);
	adminView.getProgGrid().setText(0, 0, "Name");
	adminView.getProgGrid().setText(0, 1, "Category");
	adminView.getProgGrid().setText(0, 2, "Description");
	adminView.getProgGrid().setText(0, 3, "CreateDate");
	adminView.getProgGrid().setText(0, 4, "Owner");
	adminView.getProgGrid().setText(0, 5, "Operation");
		
	programService.search(searchProgram,searchStart,searchEnd,resultStart, everyPageSize, new AsyncCallback<List<Program>>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			alertPanel.setContent(caught.getMessage());
			alertPanel.show();
		}

		@Override
		public void onSuccess(List<Program> result) {
			// TODO Auto-generated method stub
			for(int i=0;i<everyPageSize;i++){
				adminView.getProgGrid().setText(i+1, 0, result.get(i).getName());
				final String categoryId = result.get(i).getCategory();
				final int a = i;
				if(categoryId.length() == 0){
					adminView.getProgGrid().setText(a+1, 1, "————");
				}else if(("我的程序").equals(categoryId) || ("my program").equals(categoryId.toLowerCase())
						|| ("共享程序").equals(categoryId) || ("shared program").equals(categoryId.toLowerCase())
						|| ("系统程序").equals(categoryId) || ("system program").equals(categoryId.toLowerCase()) ){
					adminView.getProgGrid().setText(a+1, 1, categoryId);
				}else{
					categoryService.getCategory(categoryId, new AsyncCallback<Category>(){
						
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							adminView.getProgGrid().setText(a+1, 1, categoryId);
						}
		
						@Override
						public void onSuccess(Category result) {
							// TODO Auto-generated method stub
							if(result != null){
								adminView.getProgGrid().setText(a+1, 1, result.getPath());
							}else
								adminView.getProgGrid().setText(a+1, 1, "————");
						}
							
					});
				}
				if(result.get(i).getDescription().length() > 50){
					adminView.getProgGrid().setText(i+1, 2, result.get(i).getDescription().substring(0, 49)+"...");
				}else{
					adminView.getProgGrid().setText(i+1, 2, result.get(i).getDescription());
				}
				adminView.getProgGrid().setText(i+1, 3, result.get(i).getCreatedate());
				adminView.getProgGrid().setText(i+1, 4, result.get(i).getOwner());
				final String progId = result.get(i).getId();
				final String progName = result.get(i).getName();
				Label deleteProg = new Label();
				deleteProg.setTitle( Constants.adminUIMsg.deleteProg() );
				deleteProg.addStyleName("admin-user-delete");
				deleteProg.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						deletePanel.setContent( Constants.adminUIMsg.progDelete1() + progName + Constants.adminUIMsg.progDelete2() );
						deletePanel.show();
						deletePanel.getConfirmBtn().addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								Program program = new Program();
								program.setId(progId);
								programService.delete(progId, new AsyncCallback<Void>(){

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										deletePanel.hide();
										alertPanel.setContent(caught.getMessage());
										alertPanel.show();
									}

									@Override
									public void onSuccess(Void result) {
										// TODO Auto-generated method stub
										deletePanel.hide();
										alertPanel.setContent( Constants.adminUIMsg.progSuccess() );
										alertPanel.show();
									}
										
								});
							}

						});
					}

				});
				HorizontalPanel operate = new HorizontalPanel();
				operate.addStyleName("admin-user");
				operate.add(deleteProg);
				adminView.getProgGrid().setWidget(i + 1, 5, operate);
			}
		}
	});
  }
  
  /**
   * 目录路径中英文转换
   * 我的程序、系统程序、共享程序在库表中是中英文混合保存的
   * 在查询相关信息时需要查询两遍（中英各一遍）才能获取到准确的结果
   * @return newPath 转换后的目录路径
   */
  private String changeCategory(String catePath){
	String arr[] = catePath.split(">");
	String tmp = arr[0];
	String newPath = null;
	if(tmp.equals("我的程序")){
		newPath = "My Program";
		for(int i=1;i<arr.length;i++){
			newPath = newPath+arr[i];
		}
	}else if(tmp.equals("系统程序")){
		newPath = "System Program";
		for(int i=1;i<arr.length;i++){
			newPath = newPath+arr[i];
		}
	}else if(tmp.equals("共享程序")){
		newPath = "Shared Program";
		for(int i=1;i<arr.length;i++){
			newPath = newPath+arr[i];
		}
	}else if(tmp.toLowerCase().equals("my program")){
		newPath = "我的程序";
		for(int i=1;i<arr.length;i++){
			newPath = newPath+arr[i];
		}
	}else if(tmp.toLowerCase().equals("system program")){
		newPath = "系统程序";
		for(int i=1;i<arr.length;i++){
			newPath = newPath+arr[i];
		}
	}else if(tmp.toLowerCase().equals("shared program")){
		newPath = "共享程序";
		for(int i=1;i<arr.length;i++){
			newPath = newPath+arr[i];
		}
	}
	return newPath;
  }
  
  /**
   * 清空输入框
   */
  private void clearInput(){
	  adminView.getProgName().setValue(null);
	  adminView.getProgCate().setValue(null);
	  adminView.getProgStartDate().setValue(null);
	  adminView.getProgEndDate().setValue(null);
	  adminView.getProgOwner().setValue(null);
  }
  
  public boolean isSearchFlag() {
	return searchFlag;
  }

  public void setSearchFlag(boolean searchFlag) {
	this.searchFlag = searchFlag;
  }
}
