package com.guildasocial.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setSkipNullEnabled(true)
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        Converter<String, Date> stringToDate = new Converter<String, Date>() {
            @Override
            public Date convert(org.modelmapper.spi.MappingContext<String, Date> context) {
                String source = context.getSource();
                if (source == null || source.isEmpty()) {
                    return null;
                }
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.parse(source);
                } catch (ParseException e) {
                    return null;
                }
            }
        };

        modelMapper.addConverter(stringToDate);

        return modelMapper;
    }
}
