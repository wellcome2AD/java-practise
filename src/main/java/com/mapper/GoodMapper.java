package com.mapper;

import com.dto.GoodDTO;
import com.entity.GoodEntity;
import com.generated.Good;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Mapper(componentModel = "spring")
public interface GoodMapper {
    @Mapping(target = "name", source = "good_name")
    @Mapping(target = "delivery_date", source = "delivery_date")
    GoodDTO mapWithoutId(GoodEntity good);

    @Mapping(target = "good_name", source = "name")
    @Mapping(target = "delivery_date", source = "deliveryDate")
    GoodEntity mapGenerated(Good generated);

    default Date mapXMLGregorianCalendar(XMLGregorianCalendar calendar)
    {
        Date date = calendar.toGregorianCalendar().getTime();
        return date;
    }

    default String mapDate(Date date) {
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        String res = df.format(date);
        return res;
    }
}