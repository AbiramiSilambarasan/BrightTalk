package com.brighttalk.controller;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.brighttalk.model.ErrorCode;

@ControllerAdvice
public class ExceptionHandlerMethods {

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
	public ErrorCode DuplicateName(Exception e) {
		ErrorCode error = new ErrorCode();
		error.setCode("Duplicate Realm Name");
		return error;
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = InvalidAttributeValueException.class)
	public ErrorCode InvalidName(Exception e) {
		ErrorCode error = new ErrorCode();
		error.setCode("Invalid Realm Name");
		return error;

	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = NullPointerException.class)
	public ErrorCode NullPointerException(Exception e) {
		ErrorCode error = new ErrorCode();
		error.setCode("Invalid Realm Name");
		return error;
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = TypeMismatchException.class)
	public ErrorCode IdTypeNotFound(Exception e) {
		ErrorCode error = new ErrorCode();
		error.setCode("Invalid Argument");
		return error;

	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = SQLException.class)
	public ErrorCode RealmNotFound(Exception e) {
		ErrorCode error = new ErrorCode();
		error.setCode("Realm Name not found");
		return error;

	}
}
