package cn.springcloud.gray.server.resources.rest;

import cn.springcloud.gray.server.module.GrayServerModule;
import cn.springcloud.gray.server.module.domain.GrayDecision;
import cn.springcloud.gray.server.utils.PaginationUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gray/decision")
public class GrayDecisionResource {


    @Autowired
    private GrayServerModule grayServerModule;

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = {"policyId"})
    public List<GrayDecision> list(@RequestParam("policyId") Long policyId) {
        return grayServerModule.listGrayDecisionsByPolicyId(policyId);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<List<GrayDecision>> page(
            @RequestParam("policyId") Long policyId,
            @ApiParam @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Page<GrayDecision> page = grayServerModule.listGrayDecisionsByPolicyId(policyId, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(page);
        return new ResponseEntity<List<GrayDecision>>(
                page.getContent(),
                headers,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public GrayDecision info(@PathVariable("id") Long id) {
        return grayServerModule.getGrayDecision(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        grayServerModule.deleteGrayDecision(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void save(@RequestBody GrayDecision grayDecision) {
        grayServerModule.saveGrayDecision(grayDecision);
    }

}
