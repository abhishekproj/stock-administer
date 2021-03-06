package com.ty.stockadminister.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ty.stockadminister.dao.OwnerDao;
import com.ty.stockadminister.dao.StaffDao;
import com.ty.stockadminister.dao.SupplierDao;
import com.ty.stockadminister.dao.impl.SupplierDaoImpl;
import com.ty.stockadminister.dto.Orders;
import com.ty.stockadminister.dto.Owner;
import com.ty.stockadminister.dto.Staff;
import com.ty.stockadminister.dto.Stock;
import com.ty.stockadminister.dto.SupplierDto;
import com.ty.stockadminister.service.SupplierService;
import com.ty.stockadminister.util.ResponseStructure;

@Service
public class SupplierServiceImpl implements SupplierService {
	@Autowired
	SupplierDao dao;
	@Autowired
	StaffDao staffDao;
	@Autowired
	OwnerDao ownerDao;

	@Override
	public ResponseEntity<ResponseStructure<SupplierDto>> save(SupplierDto dto, String id) {

		Staff staff = null;
		ResponseStructure<SupplierDto> responseStructure = new ResponseStructure<SupplierDto>();
		Owner owner = ownerDao.getOwnerById(id);


		if (owner == null) {
			staff = staffDao.getStaffById(id);
		owner = staff.getOwner();
			
		}

		if ((staff != null || owner != null)) {
			dto.setStaff(staff);
			dto.setOwner(owner);
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("successfull");
			responseStructure.setData(dao.save(dto));
			ResponseEntity<ResponseStructure<SupplierDto>> responseEntity = new ResponseEntity<ResponseStructure<SupplierDto>>(
					responseStructure, HttpStatus.OK);
			return responseEntity;
		} else {

			responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
			responseStructure.setMessage("ID" + id + " not found");
			responseStructure.setData(null);
			ResponseEntity<ResponseStructure<SupplierDto>> responseEntity = new ResponseEntity<ResponseStructure<SupplierDto>>(
					responseStructure, HttpStatus.NOT_FOUND);
			return responseEntity;
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<SupplierDto>> getbyid(int id) {
		SupplierDto dto = dao.getbyid(id);
		if (dto != null) {
			ResponseStructure<SupplierDto> responseStructure = new ResponseStructure<SupplierDto>();
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("successfull");
			responseStructure.setData(dao.getbyid(id));
			ResponseEntity<ResponseStructure<SupplierDto>> responseEntity = new ResponseEntity<ResponseStructure<SupplierDto>>(
					responseStructure, HttpStatus.OK);
			return responseEntity;
		} else {
			ResponseStructure<SupplierDto> responseStructure = new ResponseStructure<SupplierDto>();
			responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
			responseStructure.setMessage("not found");
			responseStructure.setData(null);
			ResponseEntity<ResponseStructure<SupplierDto>> entity = new ResponseEntity<ResponseStructure<SupplierDto>>(
					responseStructure, HttpStatus.NOT_FOUND);
			return entity;
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<List<SupplierDto>>> getall() {
		ResponseStructure<List<SupplierDto>> responseStructure = new ResponseStructure<List<SupplierDto>>();
		responseStructure.setStatus(HttpStatus.OK.value());
		responseStructure.setMessage("successfull");
		responseStructure.setData(dao.getall());
		ResponseEntity<ResponseStructure<List<SupplierDto>>> responseEntity = new ResponseEntity<ResponseStructure<List<SupplierDto>>>(
				responseStructure, HttpStatus.OK);

		return responseEntity;
	}

	@Override
	public ResponseEntity<ResponseStructure<SupplierDto>> update(int id, String userid, SupplierDto supplierDto) {
		ResponseEntity<ResponseStructure<SupplierDto>> entity = null;
		Staff staff = null;
		SupplierDto supplier = dao.getbyid(id);
		Owner owner = ownerDao.getOwnerById(userid);
		if (supplier != null && owner != null && owner.getId().equals(supplier.getOwner().getId())) {
			supplier.setOwner(owner);
			supplier.setStaff(staff);
			ResponseStructure<SupplierDto> responseStructure = new ResponseStructure<SupplierDto>();
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("success");
			responseStructure.setData(dao.update(id, supplier));
			entity = new ResponseEntity<ResponseStructure<SupplierDto>>(responseStructure, HttpStatus.OK);
		} else if (owner == null) {
			staff = staffDao.getStaffById(userid);
			System.out.println(supplier);
			if (supplier != null && staff.getOwner().getId().equals(supplier.getOwner().getId())) {
				supplier.setStaff(staff);
				owner = staff.getOwner();
				supplier.setOwner(owner);
				ResponseStructure<SupplierDto> responseStructure = new ResponseStructure<SupplierDto>();
				responseStructure.setStatus(HttpStatus.OK.value());
				responseStructure.setMessage("success");
				responseStructure.setData(dao.update(id, supplier));
				entity = new ResponseEntity<ResponseStructure<SupplierDto>>(responseStructure, HttpStatus.OK);
			} else {
				ResponseStructure<SupplierDto> responseStructure = new ResponseStructure<SupplierDto>();
				responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
				responseStructure.setMessage("not found");
				responseStructure.setData(null);
				entity = new ResponseEntity<ResponseStructure<SupplierDto>>(responseStructure, HttpStatus.NOT_FOUND);

			}
		} else {
			ResponseStructure<SupplierDto> responseStructure = new ResponseStructure<SupplierDto>();
			responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
			responseStructure.setMessage("not found");
			responseStructure.setData(null);
			entity = new ResponseEntity<ResponseStructure<SupplierDto>>(responseStructure, HttpStatus.NOT_FOUND);

		}
		return entity;

	}

	@Override
	public ResponseEntity<ResponseStructure<String>> delete(int id,String oid) {
		SupplierDto dto = dao.getbyid(id);
		if (dao.delete(id)&&dto.getOwner().getId().equals(oid)) {
			ResponseStructure<String> responseStructure = new ResponseStructure<String>();
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("successfull");
			responseStructure.setData("deleted");
			ResponseEntity<ResponseStructure<String>> responseEntity = new ResponseEntity<ResponseStructure<String>>(
					responseStructure, HttpStatus.OK);
			return responseEntity;
		} else {
			ResponseStructure<String> responseStructure = new ResponseStructure<String>();
			responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
			responseStructure.setMessage("not found");
			responseStructure.setData(oid+" not an authorized person to delete");
			ResponseEntity<ResponseStructure<String>> entity = new ResponseEntity<ResponseStructure<String>>(
					responseStructure, HttpStatus.NOT_FOUND);
			return entity;
		}

	}
}
