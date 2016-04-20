package com.hydrogen.jpa;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hydrogen.model.Source;

public class ObjUtil {

	public static Timestamp now() {

		return new Timestamp(System.currentTimeMillis());
	}

}
