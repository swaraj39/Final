package com.pack.demo.DispatcherControllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.pack.demo.ModelDAO.Dashboard;
import com.pack.demo.ModelDAO.Streak;
import com.pack.demo.ModelDAO.UserDaily;
import com.pack.demo.ModelDAO.UserModel;
import com.pack.demo.Repository.DashBoardRepo;
import com.pack.demo.Repository.StreakRepo;
import com.pack.demo.Repository.UserDailyRepo;
import com.pack.demo.Repository.UserRepo;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/get")
@Controller
public class GetController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserDailyRepo userDailyRepo;

    @Autowired
    private DashBoardRepo dashBoardRepo;

    @Autowired
    private StreakRepo streakRepo;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadPdf(Authentication authentication) {
        try {
            String name = authentication.getName();
            UserModel userModel = userRepo.findById(name).orElseThrow();
            List<UserDaily> u = userDailyRepo.findByUserId(name);
            List<Dashboard> list = dashBoardRepo.findByUsersId(name);

            List<Dashboard> l = list.stream()
                    .map(a -> {
                        a.setStart(LocalTime.of(0, a.getStart().getMinute(), a.getStart().getSecond()));
                        a.setEnd(LocalTime.of(0, a.getEnd().getMinute(), a.getEnd().getSecond()));
                        return a;
                    })
                    .collect(Collectors.toList());

            Streak s = streakRepo.findByUserId(name);

            // Render HTML using Thymeleaf
            Context context = new Context();
            context.setVariable("user", userModel);
            context.setVariable("daily", u);
            context.setVariable("list", l);
            context.setVariable("streak", s);
            context.setVariable("avatar", userModel.getAvatar() == null ? "Avtar1.png" : userModel.getAvatar());

            // This assumes templateEngine is injected via @Autowired or constructor
            String html = templateEngine.process("Report", context);

            // Generate PDF
            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(pdfStream);
            builder.run();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfStream.toByteArray());
            String n = name + "_Report.pdf";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(n)
                    .build());
            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
