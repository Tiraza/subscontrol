package br.com.subscontrol.infraestructure.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "subprovider")
@Tag(name = "SubProvider")
public interface SubProviderAPI {
}
