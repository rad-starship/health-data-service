/**
 * 
 */
package com.rad.server.health.repositories;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;
import com.rad.server.health.entities.*;

@Repository
public interface CoronaRepository extends CrudRepository<CoronaVirusData, Long>
{
}