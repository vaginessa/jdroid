package com.jdroid.javaweb.sample.api.controller;

import com.firebase.security.token.TokenGenerator;
import com.firebase.security.token.TokenOptions;
import com.jdroid.java.http.AbstractHttpService;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.RandomUtils;
import com.jdroid.javaweb.api.AbstractController;
import com.jdroid.javaweb.sample.firebase.SampleFirebaseEntity;
import com.jdroid.javaweb.sample.firebase.SampleFirebaseRepository;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/firebase")
public class FirebaseController extends AbstractController {

	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractHttpService.class);

	private SampleFirebaseRepository repository = new SampleFirebaseRepository();

	private static String lastId;
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MimeType.TEXT)
	@ResponseBody
	public String getAll() {
		return marshall(repository.getAll().toString());
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public void add() {
		SampleFirebaseEntity entity = new SampleFirebaseEntity();
		lastId = RandomUtils.getLong().toString();
		entity.setId(lastId);
		entity.setField(RandomUtils.getLong().toString());
		repository.add(entity);
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public void update() {
		SampleFirebaseEntity entity = new SampleFirebaseEntity();
		entity.setId(lastId);
		entity.setField(RandomUtils.getLong().toString());
		repository.update(entity);	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public void remove() {
		repository.remove(lastId);
		lastId = null;
	}

	@RequestMapping(value = "/removeAll", method = RequestMethod.GET)
	public void removeAll() {
		repository.removeAll();
		lastId = null;
	}

	@RequestMapping(value = "/generateToken", method = RequestMethod.GET, produces = MimeType.TEXT)
	@ResponseBody
	public String generateToken(@RequestParam String firebaseSecret) {
		Map<String, Object> authPayload = new HashMap<>();

		TokenOptions tokenOptions = new TokenOptions();
		tokenOptions.setAdmin(true);

		TokenGenerator tokenGenerator = new TokenGenerator(firebaseSecret);
		return tokenGenerator.createToken(authPayload, tokenOptions);
	}
}