package com.vsay.microservice.provider.web;

import com.vsay.microservice.provider.Provider;
import com.vsay.microservice.provider.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProviderController {

	private ProviderRepository providerRepository;

	@Autowired
	public ProviderController(ProviderRepository providerRepository) {
		this.providerRepository = providerRepository;
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView customer(@PathVariable("id") long id) {
		return new ModelAndView("provider", "customer",
				providerRepository.findOne(id));
	}

	@RequestMapping("/list.html")
	public ModelAndView customerList() {
		return new ModelAndView("customerlist", "customers",
				providerRepository.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView add() {
		return new ModelAndView("provider", "customer", new Provider());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.POST)
	public ModelAndView post(Provider provider, HttpServletRequest httpRequest) {
		provider = providerRepository.save(provider);
		return new ModelAndView("success");
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.PUT)
	public ModelAndView put(@PathVariable("id") long id, Provider provider,
			HttpServletRequest httpRequest) {
		provider.setId(id);
		providerRepository.save(provider);
		return new ModelAndView("success");
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") long id) {
		providerRepository.delete(id);
		return new ModelAndView("success");
	}

}
