package br.com.subscontrol.infraestructure.api;

import br.com.subscontrol.ControllerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = SubProviderAPI.class)
class SubProviderAPITest {

    @Autowired
    private MockMvc mvc;


}
