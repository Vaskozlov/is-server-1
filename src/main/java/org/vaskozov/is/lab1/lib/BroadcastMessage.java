package org.vaskozov.is.lab1.lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vaskozov.is.lab1.bean.Person;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastMessage {
    PersonState state;
    Person person;
}
