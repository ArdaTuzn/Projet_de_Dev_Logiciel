package pdl.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class ImageController {

  @Autowired
  private ObjectMapper mapper;

  private final ImageDao imageDao;

  public ImageController(ImageDao imageDao) {
    this.imageDao = imageDao;
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE) 
  public ResponseEntity<?> getImage(@PathVariable long id) {
    return imageDao.retrieve(id)
        .map(img -> {
            byte[] bytes = img.getData();
            return ResponseEntity
              .ok()
              .contentType(MediaType.IMAGE_JPEG)
              .body(bytes);
          })
          .orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteImage(@PathVariable long id) {
    return imageDao.retrieve(id) 
        .map(img -> {
            imageDao.delete(img); 
            return ResponseEntity.noContent().build(); 
        })
        .orElse(ResponseEntity.notFound().build()); 
    }

  @RequestMapping(value = "/images", method = RequestMethod.POST)
  public ResponseEntity<?> addImage(@RequestParam MultipartFile file,
      RedirectAttributes redirectAttributes) throws IOException {
        if (!MediaType.IMAGE_JPEG_VALUE.equals(file.getContentType())) {
          return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        String nameImg = file.getName();
        byte[] content = file.getBytes();
        Image img = new Image(nameImg, content);
        imageDao.create(img);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/images", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public ArrayNode getImageList() {
    ArrayNode nodes = mapper.createArrayNode();
    List<Image> imageList = imageDao.retrieveAll();
    
    for (Image img : imageList) {
        ObjectNode imageNode = mapper.createObjectNode();
        imageNode.put("id", img.getId());
        imageNode.put("name", img.getName()); 
        nodes.add(imageNode);
    }
    return nodes;
  }

}
