<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
response.setStatus(301);
response.setHeader( "Location", "/ginco-webservices/services");
response.setHeader( "Connection", "close" );
%>