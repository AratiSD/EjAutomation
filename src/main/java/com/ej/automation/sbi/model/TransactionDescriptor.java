package com.ej.automation.sbi.model;

import java.util.Arrays;
import java.util.List;

public class TransactionDescriptor {
	private String parameter;
	private String operation;
	private String[] contains;
	private String[] excludes;
	private String[] containsEither;
	private String splitOn;
	private int foundAtIndex;
	private int foundOnLine;
	private boolean trimMultipleSpaces;
	private String replaceThis;
	private String replaceWith;
	private int substringStart;
	private int substringEnd;
	private String value;
	private boolean dontWriteIfExists;
	
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String[] getContains() {
		return contains;
	}
	public void setContains(String[] contains) {
		this.contains = contains;
	}
	public String[] getExcludes() {
		return excludes;
	}
	public void setExcludes(String[] excludes) {
		this.excludes = excludes;
	}
	public String[] getContainsEither() {
		return containsEither;
	}
	public void setContainsEither(String[] containsEither) {
		this.containsEither = containsEither;
	}
	public String getSplitOn() {
		return splitOn;
	}
	public void setSplitOn(String splitOn) {
		this.splitOn = splitOn;
	}
	public int getFoundAtIndex() {
		return foundAtIndex;
	}
	public void setFoundAtIndex(int foundAtIndex) {
		this.foundAtIndex = foundAtIndex;
	}
	public int getFoundOnLine() {
		return foundOnLine;
	}
	public void setFoundOnLine(int foundOnLine) {
		this.foundOnLine = foundOnLine;
	}
	public boolean isTrimMultipleSpaces() {
		return trimMultipleSpaces;
	}
	public void setTrimMultipleSpaces(boolean trimMultipleSpaces) {
		this.trimMultipleSpaces = trimMultipleSpaces;
	}
	public String getReplaceThis() {
		return replaceThis;
	}
	public void setReplaceThis(String replaceThis) {
		this.replaceThis = replaceThis;
	}
	public String getReplaceWith() {
		return replaceWith;
	}
	public void setReplaceWith(String replaceWith) {
		this.replaceWith = replaceWith;
	}
	public int getSubstringStart() {
		return substringStart;
	}
	public void setSubstringStart(int substringStart) {
		this.substringStart = substringStart;
	}
	public int getSubstringEnd() {
		return substringEnd;
	}
	public void setSubstringEnd(int substringEnd) {
		this.substringEnd = substringEnd;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isDontWriteIfExists() {
		return dontWriteIfExists;
	}
	public void setDontWriteIfExists(boolean dontWriteIfExists) {
		this.dontWriteIfExists = dontWriteIfExists;
	}
	@Override
	public String toString() {
		return "TransactionDescriptor [parameter=" + parameter + ", operation="
				+ operation + ", contains=" + Arrays.toString(contains)
				+ ", excludes=" + Arrays.toString(excludes)
				+ ", containsEither=" + Arrays.toString(containsEither)
				+ ", splitOn=" + splitOn + ", foundAtIndex=" + foundAtIndex
				+ ", foundOnLine=" + foundOnLine + ", trimMultipleSpaces="
				+ trimMultipleSpaces + ", replaceThis=" + replaceThis
				+ ", replaceWith=" + replaceWith + ", substringStart="
				+ substringStart + ", substringEnd=" + substringEnd
				+ ", value=" + value + ", dontWriteIfExists="
				+ dontWriteIfExists + "]";
	}
	
	
	
}
