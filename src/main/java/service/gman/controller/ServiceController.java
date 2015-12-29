package service.gman.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ServiceController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {

        List test = new ArrayList();
        for (int i = 0; i < 1000; i++) {
            test.add(i);
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("test", test);


        return mav;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String upload() {

        return "upload";
    }

    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadFiles(@RequestParam("file") MultipartFile[] files) {

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                uploadSingleFile(file);
            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }


    @Value("${multipart.location}") String uploadPath;

    private void uploadSingleFile(MultipartFile file) {
        String name = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File( uploadPath + name)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
                logger.warn("You failed to upload " + name + " => " + e.getMessage());
            }
        } else {
            logger.warn("You failed to upload " + name + " because the file was empty.");
        }
    }
}
