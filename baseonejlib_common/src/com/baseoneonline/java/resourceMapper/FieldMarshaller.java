package com.baseoneonline.java.resourceMapper;

interface FieldMarshaller<T>
{

	T unmarshall(String value) throws ResourceMapperException;

	String marshall(Object value);
}