package ru.dataart.courses.cassandra.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"integration", "production"})
public @interface RepositoryProfile {
}
