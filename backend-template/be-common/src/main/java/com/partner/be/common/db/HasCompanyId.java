package com.partner.be.common.db;

/**
 * Interface for entities that belong to a company
 *
 * Provides company ID for multi-tenancy support.
 */
public interface HasCompanyId {
  String getCompanyId();

  void setCompanyId(String companyId);
}
