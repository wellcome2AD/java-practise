package com.mapper;

import com.dto.GoodDTO;
import com.entity.GoodEntity;
import com.generated.Good;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Date;
import java.util.GregorianCalendar;

@Mapper(componentModel = "spring")
public interface GoodMapper {
    @Mapping(target = "name", source = "good_name")
    @Mapping(target = "delivery_date", source = "delivery_date")
    GoodDTO mapWithoutId(GoodEntity good);

    @Mapping(target = "good_name", source = "name")
    @Mapping(target = "delivery_date", source = "deliveryDate")
    GoodEntity mapGenerated(Good generated);

    default Date map(XMLGregorianCalendar calendar)
    {
        return new java.sql.Date(calendar.toGregorianCalendar().getTime().getTime());
    }

    default String mapXMLGregorianCalendar(XMLGregorianCalendar date) {
        return date.toString();
    }

    default XMLGregorianCalendar mapDate(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        DatatypeFactory dTF = null;
        try {
            dTF = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        return dTF.newXMLGregorianCalendar(gregorianCalendar);
    }
}