package com.example.final_project.utilities.mappers;

import com.example.final_project.database.entities.meeting.Meeting;
import com.example.final_project.dto.MeetingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface user to generate Meeting Mapper class
 * To 1)Move from MeetingDTO type to Meeting type
 *    2)Move from Meeting type to MeetingDTO type
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MeetingMapper {

    MeetingDTO meetingToMeetingDTO(Meeting meeting);
    Meeting meetingDTOToMeeting(MeetingDTO meetingDTO);
}

