package co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

public record RegisterUserInputDTO(UUID idType, String idNumber, String firstName, 
		String secondName, String firstSurname,
		String secondSurname, UUID homeCity, String email, String mobileNumber) {

	public static RegisterUserInputDTO normalize(final UUID idType, final String idNumber, final String firstName, 
			final String secondName, final String firstSurname,
			final String secondSurname, final UUID homeCity, final String email, final String mobileNumber){
		var idTypeNormalize = UUIDHelper.getDefault(idType);
		var idNumberNormalize = TextHelper.getDefaultWithTrim(idNumber);
		var firstNameNormalize = TextHelper.getDefaultWithTrim(firstName);
		var secondNameNormalize = TextHelper.getDefaultWithTrim(secondName);
		var firstSurnameNormalize = TextHelper.getDefaultWithTrim(firstSurname);
		var secondSurnameNormalize = TextHelper.getDefaultWithTrim(secondSurname);
		var homeCityNormalize = UUIDHelper.getDefault(homeCity);
		var emailNormalize = TextHelper.getDefaultWithTrim(email);
		var mobileNumberNormalize = TextHelper.getDefaultWithTrim(mobileNumber);
		
		return new RegisterUserInputDTO(idTypeNormalize, idNumberNormalize, firstNameNormalize, 
				secondNameNormalize, firstSurnameNormalize, secondSurnameNormalize, 
				homeCityNormalize, emailNormalize, mobileNumberNormalize);
	}
}
	
