package com.book.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.book.pojo.BookInfo;
import com.book.pojo.Pager;
import com.book.service.BookInfoService;
import com.book.service.BookInfoServiceImpl;
import com.mysql.jdbc.StringUtils;

/**
 * Servlet implementation class BookController
 */
@WebServlet("/BookController")
public class BookController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private BookInfoService bis = new BookInfoServiceImpl();  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String op = request.getParameter("op");
		if ("show".equals(op)) {
			showInfo(request,response);
		}else if("save".equals(op)) {
			saveBook(request,response);
		}else if("delete".equals(op)) {
			Delete(request,response);
		}else if("FindById".equals(op)) {
			FindByid(request,response);
		}else if("update".equals(op)) {
			update(request,response);
		}
	}

	private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		int id=Integer.parseInt(request.getParameter("id"));
		String bookName=request.getParameter("bookName");
		String author=request.getParameter("author");
		int categoryId=Integer.parseInt(request.getParameter("categoryId"));
		String publisher=request.getParameter("publisher");
		Double price=Double.parseDouble(request.getParameter("price"));
		String photo=request.getParameter("photo");
		BookInfo bookinfo=new BookInfo(bookName,author,categoryId,publisher,price,photo);
		boolean isOk=bis.update(bookinfo);
		if(isOk) {
			response.sendRedirect("admin/book-mgr.jsp");
		}
	}

	private void FindByid(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		int id=Integer.parseInt(request.getParameter("id"));
		BookInfo info=bis.findByid(id);
		request.getSession().setAttribute("info", info);
		response.sendRedirect("admin/book-edit.jsp");
	}

	private void Delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		int id=Integer.parseInt(request.getParameter("id"));
		boolean isOk=bis.delete(id);
		if(isOk) {
			response.sendRedirect("admin/book-mgr.jsp");
		}
		
	}

	private void saveBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String bookName = null;
		String author = null;
		String categoryId = null;
		String publisher =null;
		String price =null;
		String photo = null;
		String filePath=this.getServletContext().getRealPath("/file");
		boolean isMultipart = org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload.isMultipartContent(request);
		if(isMultipart) {
			FileItemFactory fac=new DiskFileItemFactory();
			ServletFileUpload upload=new ServletFileUpload(fac);
			upload.setFileSizeMax(10*1024*1024);
			try {
				List<FileItem> items =upload.parseRequest(request);
				Iterator<FileItem> it=items.iterator();
				while(it.hasNext()) {
					FileItem item=it.next();
					if(item.isFormField()) {
						String name=item.getFieldName();
						switch(name) {
							case "bookName ":
								bookName =item.getString("UTF-8");
								break;
							case "author":
								author=item.getString("UTF-8");
								break;
							case "categoryId":
								categoryId=item.getString("UTF-8");
								break;
							case "publisher":
								publisher=item.getString("UTF-8");
								break;
							case "price":
								price=item.getString("UTF-8");
								break;
						}	
					}else {
						photo=item.getName();
						  File saveFile=new File(filePath,photo);
						  item.write(saveFile);
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		BookInfo info=new BookInfo(bookName,author,Integer.parseInt(categoryId),publisher,Double.parseDouble(price),photo);
		boolean isOk=bis.save(info);
		if(isOk) {
			response.sendRedirect("admin/book-mgr.jsp");
		}else {
			response.sendRedirect("admin/book-mgr.jsp");
		}
	}

	private void showInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		showPageList(request, response);
		response.sendRedirect("user/index.jsp");
	}

	private void showPageList(HttpServletRequest request, HttpServletResponse response) {
		String pageIndex = request.getParameter("pageIndex");
		String bookName = request.getParameter("bookName");
		int currPage = 0;
		Pager pg = new Pager();
		int totalCount = bis.getcount(bookName);
		pg.setTotalCount(totalCount);
		if (StringUtils.isNullOrEmpty(pageIndex)) {
			currPage = 1;
		}else {
			if(Integer.parseInt(pageIndex)<=0) {
				currPage = 1;
			}else if(Integer.parseInt(pageIndex)>=pg.getTotalPages()) {
				currPage = pg.getTotalPages();
			}else {
				currPage = Integer.parseInt(pageIndex);
			}
		}
		pg.setCurrPage(currPage);
		// 计算from
		int from = (currPage-1)*pg.getPageSize();
		List list = bis.getBookList(bookName, from, pg.getPageSize());
		pg.setPageLists(list);
		// 将分页类实体放入到作用域中
		request.getSession().setAttribute("pg", pg);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
