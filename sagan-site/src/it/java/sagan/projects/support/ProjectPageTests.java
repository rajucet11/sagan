package sagan.projects.support;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import saganx.AbstractIntegrationTests;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectPageTests extends AbstractIntegrationTests {

    private ResultActions result;

    @Before
    public void setup() throws Exception {
        result = mockMvc.perform(MockMvcRequestBuilders.get("/project/spring-boot"));
    }

    @Test
    public void getStatusOk() throws Exception {
        result.andExpect(status().isOk());
    }

    @Test
    public void getContentType() throws Exception {
        result.andExpect(content().contentTypeCompatibleWith("text/html"));
    }

    @Test
    public void getSidebar() throws Exception {
        Document document = Jsoup.parse(result.andReturn().getResponse().getContentAsString());
        List<String> titles = document.select(".sidebar .sidebar_project a").stream()
                .map(Element::text).collect(toList());
        assertThat(titles, hasItems("Spring Data", "Spring Framework", "Spring Boot"));
        assertThat(titles, not(hasItems("Spring XD")));
    }

    @Test
    public void sidebarHighlightCurrentProject() throws Exception {
        Document document = Jsoup.parse(result.andReturn().getResponse().getContentAsString());
        List<String> titles = document.select(".sidebar .sidebar_project.active").stream()
                .map(Element::text).collect(toList());
        assertThat(titles.size(), is(1));
        assertThat(titles.get(0), is("Spring Boot"));
    }
}
