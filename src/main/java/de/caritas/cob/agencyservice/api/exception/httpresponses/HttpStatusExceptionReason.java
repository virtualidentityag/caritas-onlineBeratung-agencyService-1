package de.caritas.cob.agencyservice.api.exception.httpresponses;

public enum HttpStatusExceptionReason {
  INVALID_POSTCODE,
  INVALID_DEMOGRAPHICS_EMPTY_GENDERS,
  INVALID_DEMOGRAPHICS_EMPTY_AGE_FROM,
  INVALID_DEMOGRAPHICS_NULL_OBJECT,
  INVALID_DIOCESE,
  INVALID_CONSULTING_TYPE,
  INVALID_OFFLINE_STATUS,
  LOCKED_CONSULTING_TYPE,
  AGENCY_CONTAINS_CONSULTANTS,
  AGENCY_CONTAINS_NO_CONSULTANTS,
  AGENCY_IS_ALREADY_TEAM_AGENCY,
  AGENCY_IS_ALREADY_DEFAULT_AGENCY,
  AGENCY_ACCESS_DENIED, AGENCY_IS_LOCKED
}
