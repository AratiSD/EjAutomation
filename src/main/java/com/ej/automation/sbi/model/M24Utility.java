/******************************************************************
 * COMPANY    - FSS Pvt. Ltd.
 *****************************************************************

 Name of the Program		: M24Utility
 Description 				: Utility Class used in Msg24
 Classes Referred to		:
 Incoming parameters 		:
 Outgoing parameters 		:
 Tables Used               	:
 Values from Session		: N/A
 Values to Session			: N/A
 Created by					: Aravindan. G, Sankar P	Created Date	: 31/01/2006
 Modified by				: Parthasarathi.M				Modified Date	: 25/03/2009
 Reason for Modification	: String to Byte/ Byte to String / Alpha to hexa conversion methods added for File distribution
 Modified tag/ CR No.		:

 */

package com.ej.automation.sbi.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import javax.crypto.Cipher;

import org.springframework.stereotype.Component;

@Component
public class M24Utility {

	private static String digits = "0123456789ABCDEF";

	public static synchronized String getAuthId() throws Exception {
		final BufferedReader bufferedReader = new BufferedReader(new FileReader("authid.txt"));
		int intAuthId = Integer.parseInt(bufferedReader.readLine());
		// int intRRN = Integer.parseInt (bufferedReader.readLine ());
		bufferedReader.close();
		if (999999 == intAuthId) {// if64
			intAuthId = 0;
		} // if64
		String strAuthId = String.valueOf(++intAuthId);
		strAuthId = ("000000" + strAuthId).substring(strAuthId.length());
		final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("authid.txt"));
		bufferedWriter.write(strAuthId);
		bufferedWriter.close();
		return strAuthId;
	}

	/**
	 * <i>Rounds the Floating Number in the given String</i>
	 * <p>
	 * This is an utility method used for building ISO 8583 Message
	 * <p>
	 */
	public static String roundingFloatValue(final String ilosgStr) {
		char lochChar = 0;
		String losgDecStr = null;
		String olosgValue = null;
		olosgValue = ilosgStr;
		int lonuVal = 0;
		int lonuIndex = 0;
		int lonuIntVal = 0;
		if (olosgValue.indexOf(".") != -1) {// if54
			lonuIndex = olosgValue.indexOf(".");
			losgDecStr = olosgValue.substring(lonuIndex + 1);
			if (losgDecStr.length() > 2) {// if55
				lochChar = losgDecStr.charAt(2);
				if (Integer.parseInt(String.valueOf(lochChar)) > 5) {// if56
					lonuVal = Integer.parseInt(losgDecStr.substring(0, 2));
					lonuVal = lonuVal + 1;
					if (String.valueOf(lonuVal).length() > 2) {// if57
						lonuIntVal = Integer.parseInt(olosgValue.substring(0, lonuIndex)) + 1;
						olosgValue = String.valueOf(lonuIntVal) + ".00";
					} else {// else09
						olosgValue = olosgValue.substring(0, lonuIndex) + "." + String.valueOf(lonuVal);
					}
				} else {// else10
					olosgValue = olosgValue.substring(0, lonuIndex + 3);
				}
			} // if55
		} // if54

		return olosgValue;
	}

	/* Decimalization Table for Pin generation */
	String decimalTable[][] = new String[16][16];

	/* String array to send as response */
	String response[];

	/** The Cipher Object(Single DES or Triple DES). */
	Cipher des = null;

	/** DES Key. */
	// RawSecretKey desKey = null;

	/** DES Key. */
	// RawSecretKey localMasterKey = null;

	/**
	 * <i>Converts the Given Hours,Minitues, Sec to Seocnds</i>
	 * <p>
	 * This is an utility method used for converstion
	 * <p>
	 */
	public int convertHrsMinsSecsToSecs(final String ilosgHrs, final String ilosgMins, final String ilosgSecs) {
		try {
			if ((ilosgHrs == null) || (ilosgMins == null) || (ilosgSecs == null)) {// if27
				return 0;
			} // if27
			int lonumSecs = 0;
			lonumSecs = (Integer.parseInt(ilosgHrs.trim())) * 60 * 60 + (Integer.parseInt(ilosgMins.trim())) * 60
					+ Integer.parseInt(ilosgSecs.trim());
			return lonumSecs;
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * <i>Converts the Given Hours and Minitues to Minutes</i>
	 * <p>
	 * This is an utility method used for converstion
	 * <p>
	 */
	public int convertHrsMinsToMins(final String ilosgHrsMins) {
		try {
			if (ilosgHrsMins == null) {// if22
				return 0;
			} // if22
			String losgHrs;
			String losgMins;
			if (ilosgHrsMins.indexOf(":") == -1) {// if23// for Input
				// String Format = HHMM
				losgHrs = ilosgHrsMins.trim().substring(0, 2);
				losgMins = ilosgHrsMins.trim().substring(2);
			} else {// else01 //for Input String Format = HH:MM
				losgHrs = ilosgHrsMins.trim().substring(0, 2);
				losgMins = ilosgHrsMins.trim().substring(3);
			}
			int lonumMins = 0;
			lonumMins = (Integer.parseInt(losgHrs.trim())) * 60 + Integer.parseInt(losgMins.trim());
			return lonumMins;
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * <i>Converts the Given Hours and Minitues to Minutes</i>
	 * <p>
	 * This is an utility method used for converstion
	 * <p>
	 */
	public int convertHrsMinsToMins(final String ilosgHrs, final String ilosgMins) {
		try {
			if ((ilosgHrs == null) || (ilosgMins == null)) {// if25
				return 0;
			} // if25
			int lonumMins = 0;
			lonumMins = (Integer.parseInt(ilosgHrs.trim())) * 60 + Integer.parseInt(ilosgMins.trim());
			return lonumMins;
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * <i>Converts the Given Hours and Minitues to Seconds</i>
	 * <p>
	 * This is an utility method used for converstion
	 * <p>
	 */
	public int convertHrsMinsToSecs(final String ilosgHrsMins) {
		try {
			if (ilosgHrsMins == null) {
				return 0;
			}
			String losgHrs;
			String losgMins;
			if (ilosgHrsMins.indexOf(":") == -1) {// if24 // for Input
				// String Format = HHMM
				losgHrs = ilosgHrsMins.trim().substring(0, 2);
				losgMins = ilosgHrsMins.trim().substring(2);
			} else { // else02 //for Input String Format = HH:MM
				losgHrs = ilosgHrsMins.trim().substring(0, 2);
				losgMins = ilosgHrsMins.trim().substring(3);
			}
			int lonumMins = 0;
			lonumMins = (Integer.parseInt(losgHrs.trim())) * 60 * 60 + (Integer.parseInt(losgMins.trim())) * 60;
			return lonumMins;
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * <i>Converts the Given Hours and Minitues to Seconds</i>
	 * <p>
	 * This is an utility method used for converstion
	 * <p>
	 */
	public int convertHrsMinsToSecs(final String ilosgHrs, final String ilosgMins) {
		try {
			if ((ilosgHrs == null) || (ilosgMins == null)) {// if26
				return 0;
			} // if26
			int lonumSecs = 0;
			lonumSecs = (Integer.parseInt(ilosgHrs.trim())) * 60 * 60 + (Integer.parseInt(ilosgMins.trim())) * 60;
			return lonumSecs;
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * <i>Converts the Given Hours to Minutes</i>
	 * <p>
	 * This is an utility method used for converstion
	 * <p>
	 */
	public int convertHrsToMins(final String ilosgHrs) {
		try {
			if (ilosgHrs == null) {// if20
				return 0;
			} // if20
			int lonumMins = 0;
			lonumMins = (Integer.parseInt(ilosgHrs.trim())) * 60;
			return lonumMins;
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * <i>Converts the Given Hours to Seconds</i>
	 * <p>
	 * This is an utility method used for converstion
	 * <p>
	 */
	public int convertHrsToSecs(final String ilosgHrs) {
		try {
			if (ilosgHrs == null) {// if21
				return 0;
			} // if21
			int lonumSecs = 0;
			lonumSecs = (Integer.parseInt(ilosgHrs.trim())) * 60 * 60;
			return lonumSecs;
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * <i>Returns the End Date of the Month in the format given</i>
	 */
	public String currentMonthEndDate(final String ilosgDateFormat) {
		try {
			final Calendar loobCal = Calendar.getInstance();
			final String losgDate = loobCal.getActualMaximum(Calendar.DATE) + "/" + Calendar.MONTH + "/"
					+ loobCal.get(Calendar.YEAR);
			SimpleDateFormat loobSDF = new SimpleDateFormat("dd/MM/yyyy");
			final java.util.Date loobTmpDate = loobSDF.parse(losgDate);
			loobSDF = new SimpleDateFormat(ilosgDateFormat);
			final String olosgDate = loobSDF.format(loobTmpDate);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * <i>Returns the Start Date of the Month in the format given</i>
	 */
	public String currentMonthStartDate(final String ilosgDateFormat) {
		try {
			final Calendar loobCal = Calendar.getInstance();
			final String losgDate = loobCal.getActualMinimum(Calendar.DATE) + "/" + Calendar.MONTH + "/"
					+ loobCal.get(Calendar.YEAR);
			SimpleDateFormat loobSDF = new SimpleDateFormat("dd/MM/yyyy");
			final java.util.Date loobTmpDate = loobSDF.parse(losgDate);
			loobSDF = new SimpleDateFormat(ilosgDateFormat);
			final String olosgDate = loobSDF.format(loobTmpDate);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * <i>Returns the End Date of the week in the format given</i>
	 */
	public String currentWeekEndDate(final String ilosgDateFormat) {
		try {
			final Calendar c = Calendar.getInstance();
			final Date d = new Date();
			final long l = d.getTime();
			d.setTime(l + (7 - c.get(Calendar.DAY_OF_WEEK)) * 1000 * 60 * 60 * 24);
			c.setTime(d);
			final String losgDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/"
					+ c.get(Calendar.DAY_OF_MONTH);
			final String olosgDate = formatDate(losgDate, "yyyy/mm/dd", ilosgDateFormat);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * <i>Returns the Start Date of the week in the format given</i>
	 */
	public String currentWeekStartDate(final String ilosgDateFormat) {
		try {
			final Calendar c = Calendar.getInstance();
			final Date d = new Date();
			final long l = d.getTime();
			d.setTime(l - ((c.get(Calendar.DAY_OF_WEEK) - 1) * 1000 * 60 * 60 * 24));
			c.setTime(d);
			final String losgDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/"
					+ c.get(Calendar.DAY_OF_MONTH);
			final String olosgDate = formatDate(losgDate, "yyyy/mm/dd", ilosgDateFormat);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * <i>Returns the End Date of the current year in the format given</i>
	 */
	public String currentYearEndDate(final String ilosgDateFormat) {
		try {
			final Calendar c = Calendar.getInstance();
			final String olosgDate = formatDate(c.get(Calendar.YEAR) + "12/31", "yyyy/mm/dd", ilosgDateFormat);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * <i>Returns the Start Date of the current in the format given</i>
	 */
	public String currentYearStartDate(final String ilosgDateFormat) {
		try {
			final Calendar c = Calendar.getInstance();
			final String olosgDate = formatDate(c.get(Calendar.YEAR) + "01/01", "yyyy/mm/dd", ilosgDateFormat);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * <i>Returns the Current Date in the Given Format</i>
	 * <p>
	 * This is an utility method used for get the Current date in required Format
	 * <p>
	 */
	public String dateTime(final String ilosgDateFormat) {
		try {
			final SimpleDateFormat loobSDF = new SimpleDateFormat(ilosgDateFormat);
			final String olosgDate = loobSDF.format(new java.util.Date());
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	private String decimalizePin(String pin) {
		final String[] pinInArray = new String[16];
		for (int i = 0; i < 16; i++) {
			pinInArray[i] = pin.substring(i, i + 1);
		}
		for (int i = 0; i < pinInArray.length; i++) {
			if (pinInArray[i].equalsIgnoreCase("A")) {
				pinInArray[i] = decimalTable[10][1];
			}
			if (pinInArray[i].equalsIgnoreCase("B")) {
				pinInArray[i] = decimalTable[11][1];
			}
			if (pinInArray[i].equalsIgnoreCase("C")) {
				pinInArray[i] = decimalTable[12][1];
			}
			if (pinInArray[i].equalsIgnoreCase("D")) {
				pinInArray[i] = decimalTable[13][1];
			}
			if (pinInArray[i].equalsIgnoreCase("E")) {
				pinInArray[i] = decimalTable[14][1];
			}
			if (pinInArray[i].equalsIgnoreCase("F")) {
				pinInArray[i] = decimalTable[15][1];
			}
		}
		pin = "";
		for (int i = 0; i < 16; i++) {
			pin += pinInArray[i];
		}
		return pin;
	}

	/**
	 * <i>Returns the Start Date of the required HalfYearly in the format given</i>
	 */
	public String EndDateByHalfYearly(final String ilosgDateFormat, int ilonuQno) {
		try {
			if (ilonuQno > 2) {// if41
				ilonuQno = 2;
			} // if41
			if (ilonuQno < 0) {// if42
				ilonuQno = 0;
			} // if42
			final Calendar loobCal = Calendar.getInstance();
			if (ilonuQno == 0) {// if43
				final int lonumMon = loobCal.get(Calendar.MONTH);
				if (lonumMon < 6) {// if44
					ilonuQno = 1;
				} else {// else07
					ilonuQno = 2;
				}
			} // if43
			final String losgCalMonth[] = { "06", "12" };
			final String losgCalDate[] = { "30", "31" };
			String losgDate = "";
			losgDate = losgCalDate[ilonuQno - 1] + "/" + losgCalMonth[ilonuQno - 1] + "/" + loobCal.get(Calendar.YEAR);
			SimpleDateFormat loobSDF = new SimpleDateFormat("dd/MM/yyyy");
			final java.util.Date loobTmpDate = loobSDF.parse(losgDate);
			loobSDF = new SimpleDateFormat(ilosgDateFormat);
			final String olosgDate = loobSDF.format(loobTmpDate);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * <i>Returns the Start Date of the required Quarterly Month in the format
	 * given</i>
	 */
	public String EndDateByQuarterly(final String ilosgDateFormat, int ilonuQno) {
		try {
			if (ilonuQno > 4) {// if34
				ilonuQno = 4;
			} // if34
			if (ilonuQno < 0) {// if35
				ilonuQno = 0;
			} // if35
			final Calendar loobCal = Calendar.getInstance();
			if (ilonuQno == 0) {// if36
				final int lonumMon = loobCal.get(Calendar.MONTH);
				if (lonumMon < 3) {// if37
					ilonuQno = 1;
				} else if (ilonuQno < 6) {// eif03
					ilonuQno = 2;
				} else if (ilonuQno < 9) {// eif04
					ilonuQno = 3;
				} else {// else05
					ilonuQno = 4;
				}
			} // if36
			final String losgCalMonth[] = { "03", "06", "09", "12" };
			final String losgCalDate[] = { "31", "30", "30", "31" };
			String losgDate = "";
			losgDate = losgCalDate[ilonuQno - 1] + "/" + losgCalMonth[ilonuQno - 1] + "/" + loobCal.get(Calendar.YEAR);
			SimpleDateFormat loobSDF = new SimpleDateFormat("dd/MM/yyyy");
			final java.util.Date loobTmpDate = loobSDF.parse(losgDate);
			loobSDF = new SimpleDateFormat(ilosgDateFormat);
			final String olosgDate = loobSDF.format(loobTmpDate);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	private void fillDecimalizationTable(final String decimalizationTable) {

		for (int i = 0; i < 16; i++) {
			decimalTable[i][0] = new Integer(i).toString();
		}
		for (int i = 0; i < 16; i++) {
			decimalTable[i][1] = decimalizationTable.substring(i, i + 1);
		}
		return;
	}

	@SuppressWarnings("deprecation")
	public String formatB24DateTime(final String p12, String p17) {
		String formattedDate = null;
		try {
			final java.util.Date date = new java.util.Date();
			int hr = 0;
			hr = date.getHours();
			int yr = 0;
			yr = date.getYear() + 1900;
			if (p17.substring(0, 2).equals("01") && p17.substring(2, 4).equals("01") && (hr >= 23)) {
				yr += 1;
			}
			p17 = p17 + yr;
			formattedDate = p17.substring(0, 2) + "/" + p17.substring(2, 4) + "/" + yr + " " + p12.substring(0, 2) + ":"
					+ p12.substring(2, 4) + ":" + p12.substring(4, 6);
		} catch (final Exception e) {

		}
		return formattedDate;
	}

	/**
	 * <i>Formats the Date field from the Database basedstored value to </i>
	 * <p>
	 * This is an utility method used for building the formatted date
	 * <p>
	 */
	public String formatDate(final String ilosgDate, final String ilosgFromDateFormat, final String ilosgToDateFormat) {
		try {
			SimpleDateFormat loobSDF = new SimpleDateFormat(ilosgFromDateFormat);
			final java.util.Date loobTmpDate = loobSDF.parse(ilosgDate);
			loobSDF = new SimpleDateFormat(ilosgToDateFormat);
			final String olosgDate = loobSDF.format(loobTmpDate);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	private String generateFinalPin(final int pinLength, String pin, final String randomPin) {

		final String temp = pin;
		pin = "";

		for (int i = 0; i < pinLength; i++) {
			final int k = Integer.parseInt(temp.substring(i, i + 1));
			final int h = Integer.parseInt(randomPin.substring(i, i + 1));
			pin += Integer.toString((k + h) % 10);
		}
		return pin;
	}

	public String generatePin(final String PAN, final String keyManagementKey, final int pinLength,
			final String PINOffset) throws Exception {

		String pin;
		try {
			// java.security.Security.addProvider(new cryptix.provider.Cryptix());
			try {
				pin = getHexValue(PAN, keyManagementKey);
			} catch (final Exception e) {
				return null;
			}
			fillDecimalizationTable("0123456789012345");
			pin = decimalizePin(pin);
			pin = pin.substring(0, pinLength);
			/*
			 * Random r1 = new Random(); randomPin = new
			 * Double(r1.nextDouble()).toString().substring(2,2+pinLength);
			 */
			pin = generateFinalPin(pinLength, pin, PINOffset);
			return pin;

		} catch (final Exception e) {

			response[0] = e.getMessage();
			return null;
		}
	}

	public String generateRandom() {

		String randomPin = null;

		while (true) {
			final Random r1 = new Random();

			randomPin = new Double(r1.nextDouble()).toString();

			if (randomPin.length() > 18) {
				break;
			}
		}

		return randomPin;
	}

	public String genPIN(final String ilosgCHN) {
		String ilosgPIN = "";
		try {
			String losgRandomPin = null;
			final Random loobRnd = new Random();
			losgRandomPin = new Double(loobRnd.nextDouble()).toString().substring(2, 2 + 4);
			ilosgPIN = generatePin(ilosgCHN.substring(0, 16), "6E38ECF12C027038", 4, losgRandomPin);
		} catch (final Exception e) {
		}
		return ilosgPIN;
	}

	/**
	 * <i>Formats the amount given in the String with and without decimal place</i>
	 * if the mode == 1 then the return value is formatted amount with decimal
	 * places if the mode == 2 then the return value is 12 digit formatted amount
	 * without decimal place
	 */
	public String getFormattedAmount(String ilosgStr, final int ilonuMode) {

		String olosgFAmt = "";
		int lonuIndex = 0;

		if (!ilosgStr.equals("*")) {

			if (ilonuMode == 1) {// if49
				if (ilosgStr.trim().equals("")) {
					return ilosgStr;
				}
				if (Long.parseLong(ilosgStr) == 0) {
					ilosgStr = "0.00";
					return ilosgStr;
				} else {
					ilosgStr = removeLeadingZeroes(ilosgStr);
					lonuIndex = ilosgStr.length();
					olosgFAmt = ilosgStr.substring(0, lonuIndex - 2) + "." + ilosgStr.substring(lonuIndex - 2);
					return olosgFAmt;
				}
			} // if49
			if (ilonuMode == 2) {// if50
				lonuIndex = ilosgStr.indexOf(".");
				if (lonuIndex == -1) {// if51
					ilosgStr = ilosgStr + "00";
				} // if51
				if ((ilosgStr.length() - lonuIndex) == 1) {// if52
					ilosgStr = ilosgStr + "0";
				} // if52
				olosgFAmt = removeDecimal(ilosgStr);
				olosgFAmt = leftPadZeros(olosgFAmt, 12);
				return olosgFAmt;
			} // if50
		} else {
			ilosgStr = "";
		}

		return ilosgStr;
	}

	public String getHexValue(final String pin, final String key) throws Exception {

		try {
			des = Cipher.getInstance("DES/ECB/NONE", "Cryptix");
			// desKey = new RawSecretKey("DES", Hex.fromString(key));

			// des.initEncrypt(desKey);

			// final byte[] pinInByteArray = Hex.fromString(pin);
			// final byte[] ciphertext = des.crypt(pinInByteArray);
			// return (Hex.toString(ciphertext));
		} catch (final Exception e) {
			throw e;
		}
		return null;
	}

	public String hex2binary(String hexString) {

		if (hexString == null) {
			return null;
		}

		if (hexString.length() % 2 != 0) {
			hexString = "0" + hexString;
		}

		String binary = "";
		String temp = "";

		for (int i = 0; i < hexString.length(); i++) {
			temp = "0000" + Integer.toBinaryString(Character.digit(hexString.charAt(i), 16));
			binary += temp.substring(temp.length() - 4);
		}
		return binary;
	}

	/**
	 * <i>Checks the Alphabets in the given String</i>
	 * <p>
	 * This is an utility method used for validation
	 * <p>
	 */
	public boolean isAlpha(final String ilosgStr) {
		if (null == ilosgStr) {// if15
			return false;
		} // if15
		int lonuLen;
		for (lonuLen = 0; lonuLen < ilosgStr.length(); lonuLen++) {// for08
			if (!Character.isLetter(ilosgStr.charAt(lonuLen)) && (ilosgStr.charAt(lonuLen) != ' ')) {// if16
				return false;
			} // if16
		} // for08
		return true;
	}

	/**
	 * <i>Checks the Alphabet and Numeric in the given String</i>
	 * <p>
	 * This is an utility method used for validation
	 * <p>
	 */
	public boolean isAlphaNumeric(final String ilosgStr) {
		if (null == ilosgStr) {// if17
			return false;
		} // if17
		int lonuLen;
		for (lonuLen = 0; lonuLen < ilosgStr.length(); lonuLen++) {// for09
			if (!Character.isLetter(ilosgStr.charAt(lonuLen)) && !Character.isDigit(ilosgStr.charAt(lonuLen))
					&& (ilosgStr.charAt(lonuLen) != ' ')) {// if17
				return false;
			} // if17
		} // for09
		return true;
	}

	/**
	 * <i>Checks the Numeric value</i>
	 * <p>
	 * This is an utility method used for validation
	 * <p>
	 */
	public boolean isNumeric(final String ilosgStr) {
		if (null == ilosgStr) {// if13
			return false;
		} // if13
		int lonuLen;
		for (lonuLen = 0; lonuLen < ilosgStr.length(); lonuLen++) {// for07
			if (!Character.isDigit(ilosgStr.charAt(lonuLen)) && (ilosgStr.charAt(lonuLen) != ' ')) {// if14
				return false;
			} // if14
		} // for07
		return true;
	}

	/**
	 * <i>Checks the Alphabet, Numeric, Hyphen Underscore and Dot</i>
	 * <p>
	 * This is an utility method used for validation
	 * <p>
	 */
	public boolean isSpecialChars(final String ilosgStr) {
		if (null == ilosgStr) {// if18
			return false;
		} // if18
		int lonuLen;
		for (lonuLen = 0; lonuLen < ilosgStr.length(); lonuLen++) {// for10
			if (!Character.isLetter(ilosgStr.charAt(lonuLen)) && !Character.isDigit(ilosgStr.charAt(lonuLen))
					&& (ilosgStr.charAt(lonuLen) != ' ') && (ilosgStr.charAt(lonuLen) != '-')
					&& (ilosgStr.charAt(lonuLen) != '_') && (ilosgStr.charAt(lonuLen) != '.')) {// if19
				return false;
			} // if19
		} // for10
		return true;
	}

	/**
	 * <i>Validates the Date</i>
	 * <p>
	 * This is an utility method used for validation
	 * <p>
	 */
	public boolean isValidDate(final String ilosgDateFormat, final String ilosgDate) {
		final Calendar loobCal = Calendar.getInstance();
		final SimpleDateFormat loobSDF = new SimpleDateFormat(ilosgDateFormat);
		loobSDF.setLenient(false);
		try {
			loobCal.setTime(loobSDF.parse(ilosgDate));
		} catch (final ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * <i>Validates the Card Expiry Date</i>
	 * <p>
	 * This is an utility method used for validation
	 * <p>
	 */
	public boolean isValidExpDate(final String ilosgDate) {
		try {
			final Calendar loobSysDate = Calendar.getInstance();
			loobSysDate.set(Integer.parseInt(dateTime("yyyy")), Integer.parseInt(dateTime("MM")) - 1,
					Integer.parseInt(dateTime("dd")));
			final Calendar loobExpDate = Calendar.getInstance();
			loobExpDate.set(Integer.parseInt(ilosgDate.substring(6)), Integer.parseInt(ilosgDate.substring(3, 5)) - 1,
					Integer.parseInt(ilosgDate.substring(0, 2)));
			return loobExpDate.after(loobSysDate);
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * <i>Validates the From & To date</i>
	 * <p>
	 * This is an utility method used for validation
	 * <p>
	 */
	boolean isValidFromAndToDate(final String ilosgFromDate, final String ilosgToDate) {
		try {
			final SimpleDateFormat loobSDF = new SimpleDateFormat("MM/dd/yyyy");
			loobSDF.setLenient(false);
			final java.util.Date loobFromDate = loobSDF.parse(ilosgFromDate);
			final java.util.Date loobToDate = loobSDF.parse(ilosgToDate);
			return !loobToDate.before(loobFromDate);
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * <i>Validates the From & To date</i>
	 * <p>
	 * This is an utility method used for validation
	 * <p>
	 */
	boolean isValidFromAndToDate(final String ilosgFromDate, final String ilosgToDate, final String ilosgOriginalDate) {
		try {
			final SimpleDateFormat loobSDF = new SimpleDateFormat("MM/dd/yyyy");
			loobSDF.setLenient(false);
			final java.util.Date loobFromDate = loobSDF.parse(ilosgFromDate);
			final java.util.Date loobToDate = loobSDF.parse(ilosgToDate);
			final java.util.Date iloobOriginalDate = loobSDF.parse(ilosgOriginalDate);
			// //System.out.println(iloobOriginalDate.after(loobFromDate) +"
			// "+ iloobOriginalDate.before(loobToDate));
			if (iloobOriginalDate.after(loobFromDate) && iloobOriginalDate.before(loobToDate)) {// if29
				return true;
			} else {// else03
				return false;
			}
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * <i>Validates the Start Date</i>
	 * <p>
	 * This is an utility method used for validation
	 * <p>
	 */
	public boolean isValidStartDate(final String ilosgDate) {
		try {
			final Calendar loobSysDate = Calendar.getInstance();
			loobSysDate.set(Integer.parseInt(dateTime("yyyy")), Integer.parseInt(dateTime("MM")) - 1,
					Integer.parseInt(dateTime("dd")));
			final Calendar loobStartDate = Calendar.getInstance();
			final SimpleDateFormat loobSDF = new SimpleDateFormat("MM/dd/yyyy");
			loobSDF.setLenient(false);
			try {
				loobStartDate.setTime(loobSDF.parse(ilosgDate));
			} catch (final ParseException e) {
				return false;
			}
			if (loobStartDate.before(loobSysDate)) {// if28
				return false;
			} // if28
			loobSysDate.add(Calendar.DATE, 90);
			return loobStartDate.before(loobSysDate);
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * <i>Pads the blank space at left side</i>
	 * <p>
	 * This is an utility method used for building ISO 8583 Message
	 * <p>
	 */
	public String leftPadSpace(String ilosgStr, final int ilonuLength) {
		if (null == ilosgStr) {// if07
			ilosgStr = "";
		} // if07
		String olosgPadStr = "";
		if (ilonuLength < ilosgStr.length()) {// if08
			return ilosgStr.substring(0, ilonuLength);
		} // if08
		for (int i = ilosgStr.length(); i < ilonuLength; i++) {// for04
			olosgPadStr = ' ' + olosgPadStr;
		} // for04
		olosgPadStr = olosgPadStr + ilosgStr;
		return olosgPadStr;
	}

	/*
	 * <i>Pads the star at the left end of the given Input String and returns the
	 * same</i> <p>This is an utility method used for building ISO 8583 Message<p>
	 */
	public String leftPadStar(String ilosgStr, final int ilonuLength) {
		if (null == ilosgStr) {// if03
			ilosgStr = "";
		} // if03
		String olosgPadStr = "";
		if (ilonuLength < ilosgStr.length()) {// if04
			return ilosgStr.substring(0, ilonuLength);
		} // if04
		for (int i = ilosgStr.length(); i < ilonuLength; i++) {// for02
			olosgPadStr = "*" + olosgPadStr;
		} // for02
		olosgPadStr = olosgPadStr + ilosgStr;
		return olosgPadStr;
	}

	/**
	 * <i>Pads the zero at the begin</i>
	 * <p>
	 * This is an utility method used for building ISO 8583 Message
	 * <p>
	 * Equivalent to Pad1
	 */
	public String leftPadZeros(final String ilosgStr, final int ilonuLength) {
		if (null == ilosgStr) {// if09
			return null;
		} // if09
		String olosgPadStr = new String(ilosgStr);
		if (ilonuLength < ilosgStr.length()) {// if10
			return ilosgStr.substring(0, ilonuLength);
		} // if10
		for (int i = ilosgStr.length(); i < ilonuLength; i++) {// for05
			olosgPadStr = '0' + olosgPadStr;
		} // for05
		return olosgPadStr;
	}

	/**
	 * <i>Pads the zero at the End</i>
	 * <p>
	 * This is an utility method used for building ISO 8583 Message
	 * <p>
	 */
	String padChar(final String ilosgStr, final char ilochPadChar, final int ilonuLen, final boolean iloboAppend)
			throws Exception {
		String ilosgPadStr = new String(ilosgStr);
		if (ilonuLen < ilosgStr.length()) {
			return ilosgStr.substring(ilosgStr.length() - ilonuLen);
		}
		for (int i = ilosgStr.length(); i < ilonuLen; i++) {
			if (iloboAppend) {
				ilosgPadStr = ilosgPadStr + ilochPadChar;
			} else {
				ilosgPadStr = ilochPadChar + ilosgPadStr;
			}
		}
		return ilosgPadStr;
	}

	public String pinvalue(String str, final String pin) {
		str = (null == str) ? new String() : str;
		int inStringValue = 0;
		inStringValue = str.indexOf("-");

		if (inStringValue > 0) {
			str = str.substring(inStringValue + 1, str.length()) + pin.trim();
			return str;
		} else {
			return str;
		}
	}

	/**
	 * Method used to write stack trace to the log file
	 *
	 * @param throwable - Exception
	 * @param log       - Logger
	 */
	public void printStackTraceLog(final String terminalId, final Throwable throwable) {

		StringWriter sw = null;
		PrintWriter pw = null;

		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			throwable.printStackTrace(pw);
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
	}

	/**
	 * <i>Remove the decimal places in the String</i>
	 * <p>
	 * This is an utility method used for building ISO 8583 Message
	 * <p>
	 */
	public String removeDecimal(final String ilosgStr) {
		String olosgStr = new String();
		if (ilosgStr.indexOf(".") == -1) {// if53
			return ilosgStr + "00";
		} else {// else08
			final int k = ilosgStr.indexOf(".");
			olosgStr = ilosgStr.substring(k + 1);
			olosgStr += (olosgStr.length() == 1) ? "0" : "";
			return ilosgStr.substring(0, k) + olosgStr;
		}
	}// if53

	/**
	 * <i>Remove the Leading zeros in the String</i>
	 * <p>
	 * This is an utility method used for building ISO 8583 Message
	 * <p>
	 */
	public String removeLeadingZeroes(final String ilosgStr) {
		String olosgStr = new String();
		for (int i = 0; i < ilosgStr.length(); i++) {// for12
			if (!ilosgStr.substring(i, i + 1).equals("0")) {// if48
				olosgStr = ilosgStr.substring(i);
				break;
			} // if48
		} // for12
		return olosgStr;
	}

	/**
	 * <i>Remove the zeros in the String</i>
	 * <p>
	 * This is an utility method used for building ISO 8583 Message
	 * <p>
	 */
	public String removeZeroes(final String ilosgStr) {
		String olosgStr = new String();
		for (int i = 0; i < ilosgStr.length() - 2; i++) {// for11
			if (!ilosgStr.substring(i, i + 1).equals("0")) {// if45
				olosgStr = ilosgStr.substring(i, ilosgStr.length() - 2);
				break;
			} // if45
		} // for11
		if (Integer.parseInt(ilosgStr.substring(10, 11)) > 0) {// if46
			olosgStr += "." + ilosgStr.substring(10, 11);
		} // if46
		if (Integer.parseInt(ilosgStr.substring(11)) > 0) {// if47
			olosgStr += ilosgStr.substring(11);
		} // if47
		return olosgStr;
	}

	/*
	 * <i>Pads the blank space at right side of the given Input String and returns
	 * the same</i> <p>This is an utility method used for building ISO 8583
	 * Message<p>
	 */
	public String rightPadSpace(String ilosgStr, final int ilonuLength) {
		if (null == ilosgStr) {// if05
			ilosgStr = "";
		} // if05
		String olosgPadStr = new String(ilosgStr);
		if (ilonuLength < ilosgStr.length()) {// if06
			return ilosgStr.substring(0, ilonuLength);
		} // if06
		for (int i = ilosgStr.length(); i < ilonuLength; i++) {// for03
			olosgPadStr = olosgPadStr + ' ';
		} // for03
		return olosgPadStr;
	}

	/*
	 * <i>Pads the star at the end of the given Input String and returns the
	 * same</i> <p>This is an utility method used for building ISO 8583 Message<p>
	 */
	public String rightPadStar(String ilosgStr, final int ilonuLength) {
		if (null == ilosgStr) {// if01
			ilosgStr = "";
		} // if01
		String olosgPadStr = new String(ilosgStr);
		if (ilonuLength < ilosgStr.length()) { // if02
			return ilosgStr.substring(0, ilonuLength);
		} // if02
		for (int i = ilosgStr.length(); i < ilonuLength; i++) { // for01
			olosgPadStr = olosgPadStr + '*';
		}
		return olosgPadStr;
	}

	public String rightPadValue(String ilosgStr, final int ilonuLength) {
		if (null == ilosgStr) {// if01
			ilosgStr = "";
		} // if01
		String olosgPadStr = new String(ilosgStr);
		if (ilonuLength < ilosgStr.length()) { // if02
			return ilosgStr.substring(0, ilonuLength);
		} // if02
		for (int i = ilosgStr.length(); i < ilonuLength; i++) { // for01
			olosgPadStr = olosgPadStr + 'F';
		}
		return olosgPadStr;
	}

	/**
	 * <i>Pads the zero at the End</i>
	 * <p>
	 * This is an utility method used for building ISO 8583 Message
	 * <p>
	 */
	public String rightPadZeros(final String ilosgStr, final int ilonuLength) {

		if (null == ilosgStr) { // if11
			return null;
		} // if11

		String olosgPadStr = new String(ilosgStr);

		if (ilonuLength < ilosgStr.length()) { // if12
			return ilosgStr.substring(0, ilonuLength);
		} // if12

		for (int i = ilosgStr.length(); i < ilonuLength; i++) { // for06
			olosgPadStr = olosgPadStr + '0';
		} // for06

		return olosgPadStr;
	}

	/**
	 * <i>Returns the Start Date of the required HalfYearly in the format given</i>
	 */
	public String StartDateByHalfYearly(final String ilosgDateFormat, int ilonuQno) {
		try {
			if (ilonuQno > 2) {// if37
				ilonuQno = 2;
			} // if37
			if (ilonuQno < 0) {// if38
				ilonuQno = 0;
			} // if38
			final Calendar loobCal = Calendar.getInstance();
			if (ilonuQno == 0) {// if39
				final int lonumMon = loobCal.get(Calendar.MONTH);
				if (lonumMon < 6) {// if40
					ilonuQno = 1;
				} else {// else06
					ilonuQno = 2;
				}
			} // if39
			final String losgCalMonth[] = { "01", "06" };
			String losgDate = "";
			losgDate = loobCal.getActualMinimum(Calendar.DATE) + "/" + losgCalMonth[ilonuQno - 1] + "/"
					+ loobCal.get(Calendar.YEAR);
			SimpleDateFormat loobSDF = new SimpleDateFormat("dd/MM/yyyy");
			final java.util.Date loobTmpDate = loobSDF.parse(losgDate);
			loobSDF = new SimpleDateFormat(ilosgDateFormat);
			final String olosgDate = loobSDF.format(loobTmpDate);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * <i>Returns the Start Date of the required Quarterly Month in the format
	 * given</i>
	 */
	public String StartDateByQuarterly(final String ilosgDateFormat, int ilonuQno) {
		try {
			if (ilonuQno > 4) {// if31
				ilonuQno = 4;
			} // if31
			if (ilonuQno < 0) {// if32
				ilonuQno = 0;
			} // if32
			final Calendar loobCal = Calendar.getInstance();
			if (ilonuQno == 0) {// if33
				final int lonumMon = loobCal.get(Calendar.MONTH);
				if (lonumMon < 3) {// if34
					ilonuQno = 1;
				} else if (ilonuQno < 6) {// eif01
					ilonuQno = 2;
				} else if (ilonuQno < 9) {// eif02
					ilonuQno = 3;
				} else {// else04
					ilonuQno = 4;
				}
			} // if33
			final String losgCalMonth[] = { "01", "04", "07", "09" };
			String losgDate = "";
			losgDate = loobCal.getActualMinimum(Calendar.DATE) + "/" + losgCalMonth[ilonuQno - 1] + "/"
					+ loobCal.get(Calendar.YEAR);
			SimpleDateFormat loobSDF = new SimpleDateFormat("dd/MM/yyyy");
			final java.util.Date loobTmpDate = loobSDF.parse(losgDate);
			loobSDF = new SimpleDateFormat(ilosgDateFormat);
			final String olosgDate = loobSDF.format(loobTmpDate);
			return olosgDate;
		} catch (final Exception e) {
			return null;
		}
	}

	/* Writes to a file in case of failure in inserting transaction log */
	public void WriteTxnlogToFile_DBdown(final String query) {

		try {
			final String LABELS = "com.fss.m24.LogFile";
			final Locale locale = Locale.getDefault();
			String FILE_PATH = null;
			final ResourceBundle labels = ResourceBundle.getBundle(LABELS, locale);

			if (labels != null) {// if65
				FILE_PATH = labels.getString("FILE_PATH");
			} // if65

			final WritableByteChannel out = new FileOutputStream(FILE_PATH, true).getChannel();
			final Charset charset = Charset.forName("ISO-8859-1");

			out.write(charset.encode(query + "\n"));
			out.close();
		} catch (final Exception e) {
		}
	}

	public String removeTrailingZeros(String value) {

		String _value = value;
		if (_value.endsWith("0")) {
			_value = _value.substring(0, _value.length() - 1);
			_value = removeTrailingZeros(_value);
		}
		return _value;
	}

	public String doEncrypt(String userName, String password) {
		String encryptedPassword = new String();
		//// System.out.println("encrypted password=" + encryptedPassword);
		for (int i = 0, j = 0; i < password.length(); i++, j++) {
			if (j == userName.length())
				j = 0;
			encryptedPassword += Integer.toHexString((int) password.charAt(i) + (int) userName.charAt(j));
		}
		//// System.out.println(encryptedPassword);
		return encryptedPassword;
	}

	public static String alpha2HexC(String data) {
		char[] alpha = data.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < alpha.length; i++) {
			int count = Integer.toHexString(alpha[i]).toUpperCase().length();
			if (count <= 1) {
				sb.append(" ").append("0").append(Integer.toHexString(alpha[i]).toUpperCase());
			} else {
				sb.append(" ").append(Integer.toHexString(alpha[i]).toUpperCase());
			}
		}
		return sb.toString();
	}

	public static String convertToHex(int data) {
		String hexlen = Integer.toHexString(data).toUpperCase();
		if (hexlen.length() <= 1) {
			hexlen = "0" + Integer.toHexString(data).toUpperCase();
		}
		return hexlen;
	}

	/**
	 * @param str
	 * @return This method is used to convert the string value to byte array
	 */
	public static byte[] byteConvertor(String str) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.reset();
		int j = 0;
		int strLen = str.length();
		for (int i = 0; i < strLen; i = i + 2) {
			j = i + 2;
			baos.write((byte) Integer.parseInt(str.substring(i, j), 16));
		}
		return baos.toByteArray();
	}

	/**
	 * @param data
	 * @return This method is used to convert the string to hexa value
	 */
	public static String alpha2Hex(String data) {
		char[] alpha = data.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < alpha.length; i++) {
			int count = Integer.toHexString(alpha[i]).toUpperCase().length();
			if (count <= 1) {
				sb.append("0").append(Integer.toHexString(alpha[i]).toUpperCase());
			} else {
				sb.append(Integer.toHexString(alpha[i]).toUpperCase());
			}
		}
		return sb.toString();
	}

	/**
	 * @param ba
	 * @return
	 * @throws Exception This method is used to convert the byte array to string
	 */
	public static String ba2s(byte[] ba) throws Exception {
		try {
			StringBuffer sb = new StringBuffer();
			int off = 0;
			int len = ba.length;

			for (int i = off; i < off + len; i++) {
				int b = (int) ba[i];
				char c = digits.charAt((b >> 4) & 0xf);
				sb.append(c);
				c = digits.charAt(b & 0xf);
				sb.append((char) c);
			}
			return hex2Alpha(sb.toString());
		} catch (Exception e) {
			String expMsg = e.getMessage();
			throw new Exception(expMsg);
		}
	}

	/**
	 * @param data
	 * @return
	 * @throws Exception This method is used to convert hexa to string
	 */
	public static String hex2Alpha(String data) throws Exception {
		try {
			int len = data.length();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len; i = i + 2) {
				int j = i + 2;
				sb.append((char) Integer.valueOf(data.substring(i, j), 16).intValue());
			}
			data = sb.toString();
			return data;
		} catch (NumberFormatException e) {
			String exp = e.getMessage();
			throw new Exception(exp);
		}
	}

	/**
	 * <i>Pads the zero at the begin</i>
	 * <p>
	 * This is an utility method used for building ISO 8583 Message
	 * <p>
	 */
	public static String pad1(String str, int length) {
		str = (null == str) ? new String() : str;
		StringBuffer padStr = new StringBuffer(str);
		if (length < str.length()) {
			return str.substring(0, length);
		}
		for (int i = str.length(); i < length; i++) {
			padStr.insert(0, '0');
		}
		return padStr.toString();
	}
}// End of M24Utility
