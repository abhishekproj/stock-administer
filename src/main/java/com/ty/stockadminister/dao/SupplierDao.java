package com.ty.stockadminister.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;


import com.ty.stockadminister.dto.SupplierDto;


public interface SupplierDao {
	
	public SupplierDto save(SupplierDto dto);
	public SupplierDto getbyid(int id);
	public List<SupplierDto> getall();
	public SupplierDto update(int id,SupplierDto supplierDto);
	public boolean delete(int id);

}
