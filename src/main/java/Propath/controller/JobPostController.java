package Propath.controller;

import Propath.dto.PostJobDto;
import Propath.service.JobPostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/jobprovider")
public class JobPostController {
    @Autowired
    private JobPostService jobPostService;

    //Build save jobs REST API
    @PostMapping("/post-a-job")
    public ResponseEntity<PostJobDto> savePostJob(@RequestBody PostJobDto postJobDto, HttpServletRequest request) {
        System.out.println("Authorization: " + request.getHeader("Authorization"));
        PostJobDto savedPostJob = jobPostService.savePostJob(postJobDto);
        return new ResponseEntity<>(savedPostJob, HttpStatus.CREATED);
    }

    //Build get jobs REST API
    @GetMapping("/{id}")
    public ResponseEntity<PostJobDto> getPostJobById(@PathVariable("id") Long postId) {
        PostJobDto postJobDto = jobPostService.getPostJobById(postId);
        return new ResponseEntity<>(postJobDto, HttpStatus.OK);
    }

    //Build get all jobs REST API
    @GetMapping("/all-jobs")
    public ResponseEntity<List<PostJobDto>> getAllPostJobs() {
        List<PostJobDto> postJobDtos = jobPostService.getAllPostJobs();
        return new ResponseEntity<>(postJobDtos, HttpStatus.OK);
    }

    //Build update jobs REST API
    @PutMapping("/{id}")
    public ResponseEntity<PostJobDto> updatePostJob(@PathVariable("id") Long postId, @RequestBody PostJobDto updatedPostJob) {
        PostJobDto postJobDto = jobPostService.updatePostJob(postId, updatedPostJob);
        return new ResponseEntity<>(postJobDto, HttpStatus.OK);
    }

    //Build delete jobs REST API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostJob(@PathVariable("id") Long postId) {
        jobPostService.deletePostJob(postId);
        return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
    }
}