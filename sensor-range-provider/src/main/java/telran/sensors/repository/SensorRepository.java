package telran.sensors.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import telran.sensors.dto.SensorRangeDto;

@Slf4j
@Repository
public class SensorRepository {

	@Autowired
	MongoTemplate mongoTemplate;
	@Value("${sensor.collection.name}")
	String collectionName;
	@Value("${sensor.collection.idFieldName}")
	String idFieldName;
	@Value("${sensor.collection.minValueFieldName}")
	String minValueFieldName;
	@Value("${sensor.collection.maxValueFieldName}")
	String maxValueFieldName;
	
	public Optional<SensorRangeDto> findSensorById(String id) {
		
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where(idFieldName).is(id)),
				Aggregation.project(idFieldName, minValueFieldName, maxValueFieldName)
		);
		
		
		List<Document> resDocuments = mongoTemplate.aggregate(aggregation, collectionName, Document.class).getMappedResults();
		
		SensorRangeDto resSensorRangeDto = null;
		if(resDocuments.size() == 1) {
			Document resDocument = resDocuments.get(0);
			resSensorRangeDto = new SensorRangeDto(
					resDocument.getString(idFieldName),
					resDocument.getDouble(minValueFieldName),
					resDocument.getDouble(maxValueFieldName)
					);
		} else if (resDocuments.size() > 1) {
			log.error("SensorRepository got more then one result in method findSensorById: {}", id);
			throw new IllegalStateException(String.format("SensorRepository (findSensorById) got more then one result: %s", resDocuments.stream().map(d -> d.toJson()).collect(Collectors.joining(","))));
		}
		log.debug("SensorRepository (findSensorById method) by id: {} got: {}", id, resSensorRangeDto);
		return Optional.ofNullable(resSensorRangeDto);
	}
}
