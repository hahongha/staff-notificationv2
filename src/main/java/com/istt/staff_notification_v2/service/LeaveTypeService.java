package com.istt.staff_notification_v2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.istt.staff_notification_v2.apis.errors.BadRequestAlertException;
import com.istt.staff_notification_v2.dto.LeaveTypeDTO;
import com.istt.staff_notification_v2.dto.ResponseDTO;
import com.istt.staff_notification_v2.dto.SearchDTO;
import com.istt.staff_notification_v2.entity.LeaveType;
import com.istt.staff_notification_v2.repository.LeaveTypeRepo;

public interface LeaveTypeService {
	LeaveTypeDTO create(LeaveTypeDTO leaveTypeDTO);

	LeaveTypeDTO delete(String id);

	ResponseDTO<List<LeaveTypeDTO>> search(SearchDTO searchDTO);

	LeaveTypeDTO update(LeaveTypeDTO leaveTypeDTO);

	List<LeaveTypeDTO> getAll();

	List<LeaveTypeDTO> deleteAllbyIds(List<String> ids);

}

@Service
class LeaveTypeServiceImpl implements LeaveTypeService {

	@Autowired
	private LeaveTypeRepo leaveTypeRepo;

	private static final String ENTITY_NAME = "isttLeaveType";
	
	private static final Logger logger = LogManager.getLogger(LeaveTypeService.class);

	@Override
	@Transactional
	public LeaveTypeDTO create(LeaveTypeDTO leaveTypeDTO) {
		try {
			if (leaveTypeRepo.findByLeavetypeName(leaveTypeDTO.getLeavetypeName()).isPresent()) {
				logger.error("LeaveType Name already exists");
				throw new BadRequestAlertException("LeaveType Name already exists", ENTITY_NAME, "exist");
			}
			ModelMapper mapper = new ModelMapper();
			LeaveType leaveType = mapper.map(leaveTypeDTO, LeaveType.class);
			System.out.println("leaveType: " + leaveType);
			leaveType.setLeavetypeId(UUID.randomUUID().toString().replaceAll("-", ""));

			leaveTypeRepo.save(leaveType);
			return leaveTypeDTO;
		} catch (ResourceAccessException e) {
			logger.error(Status.EXPECTATION_FAILED);
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			logger.error(Status.SERVICE_UNAVAILABLE);
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	@Transactional
	public LeaveTypeDTO delete(String id) {
		try {
			LeaveType leaveType = leaveTypeRepo.findByLeavetypeId(id).orElseThrow(NoResultException::new);
			if (leaveType != null) {
				leaveTypeRepo.deleteById(id);
				return new ModelMapper().map(leaveType, LeaveTypeDTO.class);
			}
			logger.error("leavetype "+ id + " not found");
			return null;
		} catch (ResourceAccessException e) {
			logger.error(Status.EXPECTATION_FAILED);
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			logger.error(Status.SERVICE_UNAVAILABLE);
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public ResponseDTO<List<LeaveTypeDTO>> search(SearchDTO searchDTO) {
		try {
			List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList)
					.stream().map(order -> {
						if (order.getOrder().equals(SearchDTO.ASC))
							return Sort.Order.asc(order.getProperty());

						return Sort.Order.desc(order.getProperty());
					}).collect(Collectors.toList());
			Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

			Page<LeaveType> page = leaveTypeRepo.searchByLeavetypeName(searchDTO.getValue(), pageable);

			List<LeaveTypeDTO> leaveTypeDTOs = new ArrayList<>();
			for (LeaveType leaveType : page.getContent()) {
				LeaveTypeDTO leaveTypeDTO = new ModelMapper().map(leaveType, LeaveTypeDTO.class);
				leaveTypeDTOs.add(leaveTypeDTO);
			}

			ResponseDTO<List<LeaveTypeDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
			responseDTO.setData(leaveTypeDTOs);
			return responseDTO;
		} catch (ResourceAccessException e) {
			logger.error(Status.EXPECTATION_FAILED);
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			logger.error(Status.EXPECTATION_FAILED);
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	@Transactional
	public LeaveTypeDTO update(LeaveTypeDTO leaveTypeDTO) {
		try {
			if (leaveTypeRepo.findById(leaveTypeDTO.getLeavetypeId()).isEmpty()) {
				logger.error("LeaveType not found");
				throw new BadRequestAlertException("LeaveType not found", ENTITY_NAME, "Not found");
			}
			leaveTypeRepo.save(new ModelMapper().map(leaveTypeDTO, LeaveType.class));
			return leaveTypeDTO;
		} catch (ResourceAccessException e) {
			logger.error(Status.EXPECTATION_FAILED);
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			logger.error(Status.SERVICE_UNAVAILABLE);
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	@Transactional
	public List<LeaveTypeDTO> getAll() {
		try {
			List<LeaveType> leaveTypes = leaveTypeRepo.findAll();
			return leaveTypes.stream().map(leaveType -> new ModelMapper().map(leaveType, LeaveTypeDTO.class))
					.collect(Collectors.toList());
		} catch (ResourceAccessException e) {
			logger.error(Status.EXPECTATION_FAILED);
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			logger.error(Status.SERVICE_UNAVAILABLE);
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	@Transactional
	public List<LeaveTypeDTO> deleteAllbyIds(List<String> ids) {
		try {
			List<LeaveType> list = leaveTypeRepo.findByLeaveTypeIds(ids).orElseThrow(NoResultException::new);

			if (!list.isEmpty()) {
				leaveTypeRepo.deleteAllInBatch(list);
				return list.stream().map(leaveType -> new ModelMapper().map(leaveType, LeaveTypeDTO.class))
						.collect(Collectors.toList());
			}else {
				logger.error("LeaveType empty");
			throw new BadRequestAlertException("LeaveType empty", ENTITY_NAME, "invalid");
			}
			} catch (ResourceAccessException e) {
				logger.trace(Status.EXPECTATION_FAILED);
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			logger.trace(Status.SERVICE_UNAVAILABLE);
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

}