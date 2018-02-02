package com.mywaytec.myway.utils;

import com.mywaytec.myway.model.bean.CityModel;
import com.mywaytec.myway.model.bean.CountryModel;
import com.mywaytec.myway.model.bean.DistrictModel;
import com.mywaytec.myway.model.bean.ProvinceModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class XmlParserHandler extends DefaultHandler {

	/**
	 * 存储所有的解析对象
	 */
	private List<CountryModel> countryList = new ArrayList<CountryModel>();
	 	  
	public XmlParserHandler() {
		
	}

	public List<CountryModel> getDataList() {
		return countryList;
	}

	@Override
	public void startDocument() throws SAXException {
		// 当读到第一个开始标签的时候，会触发这个方法
	}

	CountryModel countryModel = new CountryModel();
	ProvinceModel provinceModel = new ProvinceModel();
	CityModel cityModel = new CityModel();
	DistrictModel districtModel = new DistrictModel();
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// 当遇到开始标记的时候，调用这个方法
		if (qName.equals("countryregion")) {
			countryModel = new CountryModel();
			countryModel.setName(attributes.getValue(0));
			countryModel.setProvinceList(new ArrayList<ProvinceModel>());
		} else if (qName.equals("state")) {
			provinceModel = new ProvinceModel();
			provinceModel.setName(attributes.getValue(0));
			provinceModel.setCityList(new ArrayList<CityModel>());
		} else if (qName.equals("city")) {
			cityModel = new CityModel();
			cityModel.setName(attributes.getValue(0));
			cityModel.setCode(attributes.getValue(1));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("city")) {
			provinceModel.getCityList().add(cityModel);
        } else if (qName.equals("state")) {
        	countryModel.getProvinceList().add(provinceModel);
        } else if (qName.equals("countryregion")) {
			countryList.add(countryModel);
        }
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

}
