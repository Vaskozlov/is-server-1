package org.vaskozov.is.lab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.util.PersonState;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonUpdateDTO {
    PersonState state;
    Person person;
}
