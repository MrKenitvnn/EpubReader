package com.ken.ebook.process;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import android.text.TextUtils;
import android.widget.Toast;

import com.ken.ebook.fragment.FragmentBooks;
import com.ken.ebook.model.EpubBook;

public class EpubBookHandler {
	static String epubFilePath = "";
	static String ncxFilePath = "";
	static String contentFilePath = "";
	static String coverFilePath = "";

	public static EpubBook addNewEpubBook(String pathFileEpub) {
		String bookFolder = String
				.valueOf(FragmentBooks.bookDAO.getLastId() + 1);
		EpubBook newBook = null;

		// if (!TextUtils.equals(pathFileEpub, "")) { // nếu đường dẫn khác rỗng

		// nếu file đúng định dạng .epub
		if (TextUtils.equals(FileHandler.getLastTokenizer(pathFileEpub, "."),
				"epub")) {
			// bước 1: tạo folder chứa sách
			epubFilePath = FileHandler.createBookFolder(bookFolder);
			// bước 2: giải nén
			try {
				FileHandler.doUnzip(pathFileEpub, epubFilePath); // extract
			} catch (IOException e) {
				e.printStackTrace();
			}
			// bước 3: kiểm tra xem file TOC.NCX, content.opf đã load

			ncxFilePath = FileHandler.getNcxFilePath(FileHandler.rootPath
					+ bookFolder);
			contentFilePath = FileHandler
					.getContentFilePath(FileHandler.rootPath + bookFolder);
			coverFilePath = FileHandler.getCoverFilePath(FileHandler.rootPath
					+ bookFolder);

			// nếu có 2 file content.opf && toc.ncx
			if (!TextUtils.equals(ncxFilePath, "")
					&& !TextUtils.equals(contentFilePath, "")) {

				// bước 4: thêm mới 1 sách với thư mục vừa tạo
				newBook = new EpubBook();
				newBook = JsoupParse.epubBookData(contentFilePath)
						.setEpubCover(coverFilePath)
						.setNcxFilePath(ncxFilePath).setEpubFolder(bookFolder)
						.setEpubFilePath(FileHandler.rootPath + bookFolder);

				// nếu chưa có trong CSDL mới ghi vào
				if (!FragmentBooks.bookDAO.checkABook(newBook)) {
					// rồi mới thêm vào CSDL
					FragmentBooks.bookDAO.addEpubBook(newBook);

					// bước 5: ghi chapter
					FragmentBooks.chapterDAO.addListChapter(JsoupParse
							.listEpubChapterData(newBook));
					// bước 6: ghi css
					FragmentBooks.cssDAO.addListCss(Integer.parseInt(newBook
							.getEpubFolder()), FileHandler.getAllCSS(new File(
							newBook.getEpubFilePath())));

				} else { // nếu có trong CSDL thì xóa thư mục
					// thông báo
					Toast.makeText(FragmentBooks.context,
							newBook.getEpubBookName() + " đã có",
							Toast.LENGTH_LONG).show();
					// và xóa luôn thư mục
					FileHandler.deleteBookFolder(new File(FileHandler.rootPath
							+ bookFolder));
					newBook = null;
				}
			} else { // nếu không có: thông báo và xóa luôn thư mục
				Toast.makeText(FragmentBooks.context,
						"Thiếu file coltrol, vui lòng tải lại file .epub",
						Toast.LENGTH_LONG).show();

			}// end-if

		} else { // đã xong xử lý này
			Toast.makeText(FragmentBooks.context, "Đây không phải file .epub",
					Toast.LENGTH_SHORT).show();
		}
		// }// end-if
		return newBook;
	}

}
