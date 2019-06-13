package {{Package}}.service;

import {{Package}}.domain.User;
import {{Package}}.exception.ServerErrorException;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Author: huangpeilin
 * Create at: 2018-08-13 9:42:58
 * Description:
 */
@RunWith(SpringRunner.class)
public class DemoServiceTest {
/*
    @MockBean
    private ApkManagementRepository apkManagementRepository;
    @MockBean
    private ImageService imageService;
    @MockBean
    private FileService fileService;

    private ApkManagementService apkManagementService;

    @Before
    public void beforeTest() {
        apkManagementService = new ApkManagementService(apkManagementRepository, imageService, fileService);
    }

    @Test
    public void testDownloadApkFileButDownloadIsNotNull() {
        String key = "sss";
        ApkManagement apkManagement = new ApkManagement();
        apkManagement.setDownloads(1);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(apkManagementRepository.findOneByKeyName(key)).thenReturn(apkManagement);
        apkManagementService.downloadApkFile(key, response);
    }

    @Test
    public void testDownloadApkFileButDownloadIsNull() {
        String key = "sss";
        ApkManagement apkManagement = new ApkManagement();
        apkManagement.setDownloads(null);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(apkManagementRepository.findOneByKeyName(key)).thenReturn(apkManagement);
        apkManagementService.downloadApkFile(key, response);
    }

    @Test
    public void testUploadApkFile() {
        String version = "version";
        String type = "type";
        byte[] bytes = new byte[]{1, 2, 3};
        MultipartFile file = mock(MultipartFile.class);
        JSONObject jsonResult = mock(JSONObject.class);
        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(file.getOriginalFilename()).thenReturn("wpr.apk");
        try {
            when(file.getBytes()).thenReturn(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = new User();
        ApkManagement apkManagement = new ApkManagement();
        apkManagement.setKeyName("key");
        FileResponseDTO fileResponseDTO = new FileResponseDTO();
        fileResponseDTO.setSourceDownloadUrl("url=ssss");
        fileResponseDTO.setFileName("wpr.apk");
        fileResponseDTO.setFileSize((float) 11);
        when(apkManagementRepository.findOneByVersionAndType(version, type)).thenReturn(apkManagement);
        try {
            when(imageService.uploadUnPermission(file, user, "key")).thenReturn(fileResponseDTO);
        } catch (ServerErrorException e) {
            e.printStackTrace();
        }
        try {
            apkManagementService.uploadApkFile(file, user, version, true, type);
        } catch (ServerErrorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testUploadApkFileButApkManagementIsNull() {
        String version = "version";
        String type = "type";
        byte[] bytes = new byte[]{1, 2, 3};
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("wpr.apk");
        try {
            when(file.getBytes()).thenReturn(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = new User();
        FileResponseDTO fileResponseDTO = new FileResponseDTO();
        fileResponseDTO.setSourceDownloadUrl("url=1111");
        fileResponseDTO.setFileName("wpr.apk");
        fileResponseDTO.setFileSize((float) 11);
        when(apkManagementRepository.findOneByVersionAndType(version, type)).thenReturn(null);
        try {
            when(imageService.uploadUnPermission(file, user, null)).thenReturn(fileResponseDTO);
        } catch (ServerErrorException e) {
            e.printStackTrace();
        }
        try {
            apkManagementService.uploadApkFile(file, user, version, true, type);
        } catch (ServerErrorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testUploadApkFileButFileTypeIsNotAPK() {
        String version = "version";
        String type = "type";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("wpr.java");
        User user = new User();
        try {
            apkManagementService.uploadApkFile(file, user, version, true, type);
        } catch (ServerErrorException e) {
            assertThat(e.getMessage()).isEqualTo("File is not a apk file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetTheLatestApkByType() {
        String type = "type";
        Sort sort = new Sort(Sort.Direction.DESC, "gmtModified");
        List<ApkManagement> apkManagementList = new ArrayList<>();
        ApkManagement apkManagement = new ApkManagement();
        apkManagement.setName("ll.apk");
        apkManagement.setBIsForce(true);
        apkManagement.setKeyName("keyName");
        apkManagement.setType("type");
        apkManagement.setVersion("version");
        apkManagementList.add(apkManagement);
        when(apkManagementRepository.findAllByType(type, sort)).thenReturn(apkManagementList);
        apkManagementService.getTheLatestApkByType(type);
    }

    @Test
    public void testFindAllApkManagementByType() {
        Sort sort = new Sort(Sort.Direction.DESC, "gmtModified");
        List<ApkManagement> apkManagementList = new ArrayList<>();
        ApkManagement apkManagement = new ApkManagement();
        apkManagement.setName("ll.apk");
        apkManagement.setBIsForce(true);
        apkManagement.setKeyName("keyName");
        apkManagement.setType("type");
        apkManagement.setVersion("version");
        apkManagementList.add(apkManagement);
        when(apkManagementRepository.findAllByType("type", sort)).thenReturn(apkManagementList);
        apkManagementService.findAllApkManagementByType("type");
    }

    @Test
    public void testDelete() {
        ApkManagement apkManagement = new ApkManagement();
        apkManagement.setName("ll.apk");
        apkManagement.setBIsForce(true);
        apkManagement.setKeyName("keyName");
        apkManagement.setType("type");
        apkManagement.setVersion("version");
        when(apkManagementRepository.findOneByKeyName("key")).thenReturn(apkManagement);
        try {
            apkManagementService.delete("key");
        } catch (ServerErrorException e) {
            e.printStackTrace();
        }
    }
*/
}
