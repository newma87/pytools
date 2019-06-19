package {{Package}}.controller;

import {{Package}}.common.RestRespDTO;
import {{Package}}.controllers.DemoController;
import {{Package}}.security.RolePermission;
import {{Package}}.security.jwt.TokenProvider;
import {{Package}}.util.JsonUtil;
import {{Package}}.util.RestRespUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DemoController.class)
public class DemoControllersTests {

    @MockBean
    private AuthenticationManager author;

    @MockBean
    private TokenProvider provider;

    @MockBean
    private PasswordEncoder encoder;

	@Autowired
	private MockMvc mockMvc;

	@Test
    @WithMockUser(username = "admin", roles = {RolePermission.ADMIN, RolePermission.GUEST})
	public void helloShouldWork() throws Exception {
        String obj = "hello, admin";
        RestRespDTO dto = RestRespUtil.getSuccessRsp(obj);
        String result = JsonUtil.fromObject(dto);

		mockMvc.perform(get("/apiv1/hello"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(result));
	}
}
