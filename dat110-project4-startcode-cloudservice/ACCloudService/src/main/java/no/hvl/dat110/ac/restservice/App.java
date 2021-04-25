package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;

import java.util.concurrent.atomic.AtomicInteger;

import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

/**
 * Hello world!
 *
 */
public class App {

	static AccessLog accesslog = null;
	static AccessCode accesscode = null;

	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service

		accesslog = new AccessLog();
		accesscode = new AccessCode();

		after((req, res) -> {
			res.type("application/json");
		});

		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {

			Gson gson = new Gson();

			return gson.toJson("IoT Access Control Device");
		});

		post("/accessdevice/log", (req, res) -> {

			Gson gson = new Gson();

			accesslog.add(gson.fromJson(req.body(), AccessMessage.class).getMessage());
			return accesslog.toJson();
		});

		get("/accessdevice/log", (req, res) -> {

			return accesslog.toJson();
		});

		get("/accessdevice/log/:id", (req, res) -> {
			int id = Integer.parseInt(req.params(":id"));

			AccessEntry ae = accesslog.get(id);

			return ae.toJson();
		});

		put("/accessdevice/code", (req, res) -> {

			Gson gson = new Gson();

			accesscode = gson.fromJson(req.body(), AccessCode.class);

			return accesscode.toJson();
		});

		get("/accessdevice/code", (req, res) -> {

			return accesscode.toJson();
		});

		delete("/accessdevice/log", (req, res) -> {

			accesslog.clear();
			return accesslog.toJson();
		});

	}

}
