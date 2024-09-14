package com.StudentManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.StudentManagement.dao.CourseRepository;
import com.StudentManagement.dao.StudentRepository;
import com.StudentManagement.entity.Course;
import com.StudentManagement.entity.Student;
import java.io.ByteArrayOutputStream;
import java.util.List;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
@Service
public class PDFServiceImpl implements PDFService {

	private StudentRepository studentRepository;
	private CourseRepository courseRepository;
	
	@Autowired
	public PDFServiceImpl(StudentRepository theStudentRepository, CourseRepository theCourseRepository) {
		studentRepository = theStudentRepository;
		courseRepository = theCourseRepository;
	}

	public byte[] generateCourseSchedulePdf(Long studentId) {
		
		 	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
	        PdfDocument pdfDoc = new PdfDocument(writer);
	        Document document = new Document(pdfDoc);

	        Student student = studentRepository.findById(studentId)
	                .orElseThrow(() -> new RuntimeException("Student not found"));

	        List<Course> courses = student.getCourses();

	        Table table = new Table(3);
	        table.addHeaderCell("Course ID");
	        table.addHeaderCell("Course Name");
	        table.addHeaderCell("Description");

	        for (Course course : courses) {
	            table.addCell(new Cell().add(new Paragraph(String.valueOf(course.getId()))));
	            table.addCell(new Cell().add(new Paragraph(course.getName())));
	            table.addCell(new Cell().add(new Paragraph(course.getDescription())));
	        }

	        document.add(table);
	        document.close();

	        return byteArrayOutputStream.toByteArray();	}
}
