/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.user.utente;

import java.util.Objects;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.dto.Profilo;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class Utente   {

	public static int MAX_LENGTH_NOME_COGNOME = 50;
	public static int MAX_LENGTH_EMAIL = 50;

	private Long idUtente = null;
	private String cognome = null;
	private String nome = null;
	private String codiceFiscale = null;
	private String email = null;
	private String dataInserimento = null;
	private String dataCancellazione = null;
	private Profilo profilo = null;


	public void validate()throws DatiInputErratiException {
		ValidatorDto.validateLength(getCognome(), Boolean.TRUE, MAX_LENGTH_NOME_COGNOME, null, null, null);
		ValidatorDto.validateLength(getNome(), Boolean.TRUE, MAX_LENGTH_NOME_COGNOME, null, null, null);
		ValidatorDto.validateCodiceFiscale(getCodiceFiscale(), Boolean.TRUE,  null, null);
		ValidatorDto.validateLength(getEmail(), Boolean.FALSE, MAX_LENGTH_EMAIL, null, null, null);
		ValidatorDto.validateEmail(getEmail(), Boolean.FALSE,  null, null);
		ValidatorDto.validateNullValue(getProfilo(), Boolean.TRUE, null, null);
		ValidatorDto.validateNumber(getProfilo() != null ? getProfilo().getIdProfilo():null, Boolean.TRUE, null, null);
	}

	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();
		ValidatorDto.validateNullValue(getIdUtente(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				
	}


	@ApiModelProperty(value = "Utente: Pk")

	public Long getIdUtente() {
		return idUtente;
	}
	public void setIdUtente(Long idUtente) {
		this.idUtente = idUtente;
	}

	@ApiModelProperty(value = "Utente: Data Cancellazione")

	public String getDataCancellazione() {
		return dataCancellazione;
	}
	public void setDataCancellazione(String dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	@ApiModelProperty(value = "Utente: Data Inserimento")

	public String getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	@ApiModelProperty(value = "Utente: Cognome")

	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	@ApiModelProperty(value = "Utente: Nome utente")

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	@ApiModelProperty(value = "Utente: codice fiscale")

	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@ApiModelProperty(value = "Email")

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@ApiModelProperty(value = "Profilo")
	public Profilo getProfilo() {
		return profilo;
	}
	public void setProfilo(Profilo profilo) {
		this.profilo = profilo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Utente utente = (Utente) o;
		return Objects.equals(idUtente, utente.idUtente) &&
				Objects.equals(dataCancellazione, utente.dataCancellazione) &&
				Objects.equals(cognome, utente.cognome) &&
				Objects.equals(nome, utente.nome) &&
				Objects.equals(codiceFiscale, utente.codiceFiscale) &&
				Objects.equals(email, utente.email) &&
				Objects.equals(profilo, utente.profilo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idUtente, dataCancellazione,  cognome, nome, codiceFiscale,  email,  profilo);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Utente {\n");   
		sb.append("idUtente: ").append(toIndentedString(idUtente)).append("\n");
		sb.append("cognome: ").append(toIndentedString(cognome)).append("\n");
		sb.append("nome: ").append(toIndentedString(nome)).append("\n");
		sb.append("codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
		sb.append("email: ").append(toIndentedString(email)).append("\n");
		sb.append("profilo: ").append(toIndentedString(profilo)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}


}

