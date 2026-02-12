package pdl.backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart; // İşte aradığın bu!
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class ImageControllerTests {

	@MockitoBean
	private ImageDao imageDAO;
	@MockitoBean
	private Image image;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getImageShouldReturnSuccess() throws Exception {
		when(imageDAO.retrieve(0)).thenReturn(Optional.ofNullable(image));
		this.mockMvc.perform(get("/images/0")).andExpect(status().isOk());
		verify(imageDAO).retrieve(0);
	}

	@Test
	public void getImageShouldReturnNotFound() throws Exception {
		when(imageDAO.retrieve(100)).thenReturn(Optional.empty());
		this.mockMvc.perform(get("/images/100")).andExpect(status().isNotFound());
		verify(imageDAO).retrieve(100);
	}

	@Test
	public void addImageShouldReturnSuccess() throws Exception {
		MockMultipartFile mockFile = new MockMultipartFile(
			"file",
        	"test.jpg", 
        	MediaType.IMAGE_JPEG_VALUE, 
        	"dummy image content".getBytes()
		);
		this.mockMvc.perform(multipart("/images").file(mockFile)).andExpect(status().isOk());
		verify(imageDAO).create(any(Image.class));
	}

	@Test
	public void addImageShouldReturnUnsupportedMediaType() throws Exception {
		MockMultipartFile mockFile = new MockMultipartFile(
			"file",
        	"test.png", 
        	MediaType.IMAGE_PNG_VALUE, 
        	"dummy image content".getBytes()
		);
		this.mockMvc.perform(multipart("/images").file(mockFile)).andExpect(status().isUnsupportedMediaType());
		verify(imageDAO, times(0)).create(any(Image.class));
	}

	@Test
	public void deleteImagesShouldReturnMethodNotAllowed() throws Exception {
		this.mockMvc.perform(delete("/images")).andExpect(status().isMethodNotAllowed());
		verify(imageDAO, times(0)).delete(any(Image.class));
	}

	@Test
	public void deleteImageShouldReturnNotFound() throws Exception {
		this.mockMvc.perform(delete("/images/100")).andExpect(status().isNotFound());
		verify(imageDAO, times(0)).delete(any(Image.class));
	}

	@Test
	public void deleteImageShouldReturnSuccess() throws Exception {
		when(imageDAO.retrieve(0)).thenReturn(Optional.ofNullable(image));
		this.mockMvc.perform(delete("/images/0")).andExpect(status().isNoContent());
		verify(imageDAO).delete(image);
	}
	
	@Test
	public void getImageListShouldReturnSuccess() throws Exception {
		//TO DO
	}
	
}
