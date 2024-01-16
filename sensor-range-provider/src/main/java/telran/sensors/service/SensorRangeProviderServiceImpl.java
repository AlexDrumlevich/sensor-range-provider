package telran.sensors.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.sensors.dto.SensorRangeDto;
import telran.sensors.repository.SensorRepository;
@Slf4j
@Service
public class SensorRangeProviderServiceImpl implements SensorRangeProviderService {
@Autowired
private SensorRepository sensorRepository;


	@Override
	public SensorRangeDto findSensor(String id) {
		// TODO Auto-generated method stub
		SensorRangeDto result = sensorRepository.findSensorById(id).orElseThrow(() -> new NotFoundException(String.format("Sensor id %s not found", id)));
		log.debug("SensorRepository findSensor by id: {} got {}", id, result);
		return result;
	}

}
